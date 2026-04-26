package com.plataforma.servicos.service;

import com.plataforma.servicos.dto.favoriteDTOS.FavoriteResponseDTO;
import com.plataforma.servicos.entity.FavoriteModel;
import com.plataforma.servicos.entity.ServiceModel;
import com.plataforma.servicos.entity.UserModel;
import com.plataforma.servicos.mapper.FavoriteMapper;
import com.plataforma.servicos.repository.FavoriteRepository;
import com.plataforma.servicos.repository.ServiceRepository;
import com.plataforma.servicos.repository.UserRepository;
import com.plataforma.servicos.util.PaginatedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final FavoriteMapper favoriteMapper;

    // Regra: usuário só vê seus próprios favoritos
    // Regra: usuário deve existir
    // Lista favoritos do usuário com paginação
    public PaginatedResponse<FavoriteResponseDTO> findByUsuario(
            UUID usuarioId, Pageable pageable) {
        userRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Page<FavoriteResponseDTO> page = favoriteRepository
                .findByUsuarioId(usuarioId, pageable)
                .map(favoriteMapper::toResponseDTO);
        return PaginatedResponse.of(page);
    }

    // Verifica se um serviço é favorito do usuário
    // Usado no frontend para mostrar ícone de coração cheio ou vazio
    public boolean isFavorito(UUID usuarioId, UUID serviceId) {
        return favoriteRepository
                .findByUsuarioIdAndServiceId(usuarioId, serviceId)
                .isPresent();
    }

    // Toggle de favorito (favorita/desfavorita)
    // Regra: se já favoritou → desfavorita
    // Regra: se não favoritou → favorita
    // Regra: unicidade — um usuário só pode favoritar um serviço uma vez
    // Regra: serviço deve estar ativo para ser favoritado
    // Regra: usuário não pode favoritar seu próprio serviço
    @Transactional
    public String toggle(UUID usuarioId, UUID serviceId) {
        UserModel usuario = userRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        ServiceModel service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        // Serviço inativo não pode ser favoritado
        if (Boolean.FALSE.equals(service.getAtivo())) {
            throw new RuntimeException("Serviço não está disponível");
        }

        // Usuário não pode favoritar seu próprio serviço
        if (service.getPrestador().getId().equals(usuarioId)) {
            throw new RuntimeException("Você não pode favoritar seu próprio serviço");
        }

        // Verifica se já existe o favorito
        Optional<FavoriteModel> favoritoExistente = favoriteRepository
                .findByUsuarioIdAndServiceId(usuarioId, serviceId);

        // Se já favoritou → desfavorita
        if (favoritoExistente.isPresent()) {
            favoriteRepository.delete(favoritoExistente.get());
            return "Serviço removido dos favoritos";
        }

        // Se não favoritou → favorita
        // Removido o set manual de criadoEm para usar o Spring Auditing
        FavoriteModel favorite = FavoriteModel.builder()
                .usuario(usuario)
                .service(service)
                .build();

        favoriteRepository.save(favorite);
        return "Serviço adicionado aos favoritos";
    }
}
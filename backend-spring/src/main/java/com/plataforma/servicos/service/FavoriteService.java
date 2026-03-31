package com.plataforma.servicos.service;

import com.plataforma.servicos.dto.favoriteDTOS.FavoriteResponseDTO;
import com.plataforma.servicos.mapper.FavoriteMapper;
import com.plataforma.servicos.repository.FavoriteRepository;
import com.plataforma.servicos.repository.ServiceRepository;
import com.plataforma.servicos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final FavoriteMapper favoriteMapper;

    // Lista todos os favoritos do usuário
    // Regra: usuário só vê seus próprios favoritos
    // Regra: usuário deve existir
    public List<FavoriteResponseDTO> findByUsuario(UUID usuarioId) {
        userRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return favoriteRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(favoriteMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Verifica se um serviço é favorito do usuário
    // Usado no frontend para mostrar ícone de coração cheio ou vazio
    public boolean isFavorito(UUID usuarioId, UUID serviceId) {
        return favoriteRepository
                .findByUsuarioIdAndServiceId(usuarioId, serviceId)
                .isPresent();
    }
}

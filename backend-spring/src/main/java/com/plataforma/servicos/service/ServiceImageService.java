package com.plataforma.servicos.service;

import com.plataforma.servicos.dto.serviceImagesDTO.ServiceImageRequestDTO;
import com.plataforma.servicos.dto.serviceImagesDTO.ServiceImageResponseDTO;
import com.plataforma.servicos.entity.ServiceImageModel;
import com.plataforma.servicos.entity.ServiceModel;
import com.plataforma.servicos.entity.UserENUM;
import com.plataforma.servicos.entity.UserModel;
import com.plataforma.servicos.mapper.ServiceImageMapper;
import com.plataforma.servicos.repository.ServiceImageRepository;
import com.plataforma.servicos.repository.ServiceRepository;
import com.plataforma.servicos.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceImageService {

    private final ServiceImageRepository serviceImageRepository;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final ServiceImageMapper serviceImageMapper;

    // Limite máximo de imagens por serviço
    // 10 imagens são suficientes para mostrar o trabalho
    // profissionalmente sem sobrecarregar o sistema
    private static final int MAX_IMAGENS_POR_SERVICO = 10;

    // Lista todas as imagens de um serviço
    // Regra: qualquer pessoa pode ver as imagens — são públicas
    // Regra: serviço deve existir
    // Usado no frontend para exibir galeria de imagens do serviço
    public List<ServiceImageResponseDTO> findByService(UUID serviceId) {
        serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        return serviceImageRepository.findByServiceId(serviceId)
                .stream()
                .map(serviceImageMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Adiciona imagem ao serviço
    // Regra: apenas o próprio PRESTADOR dono do serviço pode adicionar
    // Regra: serviço deve estar ativo
    // Regra: limite máximo de 10 imagens por serviço
    //        evita abuso e sobrecarga no sistema
    // Regra: URL deve ser válida e não vazia
    // No M7 o prestadorId virá do token JWT automaticamente
    // No M7 será integrado com Cloudinary para upload real
    // Por enquanto recebe a URL diretamente
    @Transactional
    public ServiceImageResponseDTO addImage(
            UUID serviceId, UUID prestadorId, ServiceImageRequestDTO dto) {

        ServiceModel service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        UserModel prestador = userRepository.findById(prestadorId)
                .orElseThrow(() -> new RuntimeException("Prestador não encontrado"));

        // Apenas o dono do serviço pode adicionar imagens
        if (!service.getPrestador().getId().equals(prestadorId)) {
            throw new RuntimeException(
                    "Você não tem permissão para adicionar imagens neste serviço"
            );
        }

        // Apenas PRESTADOR pode adicionar imagens
        if (!UserENUM.PRESTADOR.equals(prestador.getPerfil())) {
            throw new RuntimeException(
                    "Apenas prestadores podem adicionar imagens ao serviço"
            );
        }

        // Serviço desativado não pode receber novas imagens
        if (Boolean.FALSE.equals(service.getAtivo())) {
            throw new RuntimeException(
                    "Não é possível adicionar imagens em serviço desativado"
            );
        }

        // Verifica limite de imagens por serviço
        long totalImagens = serviceImageRepository.countByServiceId(serviceId);
        if (totalImagens >= MAX_IMAGENS_POR_SERVICO) {
            throw new RuntimeException(
                    "Limite máximo de " + MAX_IMAGENS_POR_SERVICO +
                            " imagens por serviço atingido"
            );
        }

        ServiceImageModel image = serviceImageMapper.toModel(dto);
        image.setService(service);

        return serviceImageMapper.toResponseDTO(serviceImageRepository.save(image));
    }
}

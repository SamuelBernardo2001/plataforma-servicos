package com.plataforma.servicos.service;

import com.plataforma.servicos.dto.serviceImagesDTO.ServiceImageResponseDTO;
import com.plataforma.servicos.mapper.ServiceImageMapper;
import com.plataforma.servicos.repository.ServiceImageRepository;
import com.plataforma.servicos.repository.ServiceRepository;
import com.plataforma.servicos.repository.UserRepository;
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
}

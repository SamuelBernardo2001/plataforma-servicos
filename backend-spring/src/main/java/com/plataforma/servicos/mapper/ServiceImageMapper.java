package com.plataforma.servicos.mapper;

import com.plataforma.servicos.dto.serviceImagesDTO.ServiceImageRequestDTO;
import com.plataforma.servicos.dto.serviceImagesDTO.ServiceImageResponseDTO;
import com.plataforma.servicos.entity.ServiceImageModel;
import org.springframework.stereotype.Component;

@Component
public class ServiceImageMapper {

    // Converte ServiceImageModel para ServiceImageResponseDTO
    public ServiceImageResponseDTO toResponseDTO(ServiceImageModel image) {
        return new ServiceImageResponseDTO(
                image.getId(),
                image.getService() != null ? image.getService().getId() : null,
                image.getUrl()
        );
    }

    // Converte ServiceImageRequestDTO para ServiceImageModel
    // service é setado no ServiceImageService
    // pois precisa ser buscado no banco pelo ID
    public ServiceImageModel toModel(ServiceImageRequestDTO dto) {
        return ServiceImageModel.builder()
                .url(dto.url())
                .build();
    }
}
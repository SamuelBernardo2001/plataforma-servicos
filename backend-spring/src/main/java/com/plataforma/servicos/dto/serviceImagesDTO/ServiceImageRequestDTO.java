package com.plataforma.servicos.dto.serviceImagesDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ServiceImageRequestDTO(

        // URL da imagem após upload no Cloudinary
        // O upload em si será implementado no M7
        // Por enquanto recebe a URL diretamente
        @NotBlank(message = "URL da imagem é obrigatória")
        @Size(max = 2048, message = "URL muito longa")
        String url

) { }
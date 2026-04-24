package com.plataforma.servicos.dto.serviceImagesDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Objeto para vincular uma imagem enviada ao catálogo de um serviço")
public record ServiceImageRequestDTO(

        @Schema(description = "URL pública da imagem (gerada pelo Cloudinary ou serviço similar)",
                example = "https://res.cloudinary.com/demo/image/upload/v1234567/servico_reforma.jpg",
                maxLength = 2048,
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "URL da imagem é obrigatória")
        @Size(max = 2048, message = "URL muito longa")
        String url

) { }
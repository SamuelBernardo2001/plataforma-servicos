package com.plataforma.servicos.dto.serviceImagesDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "Resposta contendo os dados da imagem vinculada a um serviço")
public record ServiceImageResponseDTO(

        @Schema(description = "Identificador único da imagem no banco de dados (UUID)",
                example = "b2b1c3d4-e5f6-4372-a567-0e02b2c3d479")
        UUID id,

        @Schema(description = "ID do serviço ao qual esta imagem pertence",
                example = "722f9923-388e-473d-9d7a-111122223333")
        UUID serviceId,

        @Schema(description = "URL completa da imagem para renderização no frontend",
                example = "https://res.cloudinary.com/demo/image/upload/v1234567/servico_reforma.jpg")
        String url

) { }
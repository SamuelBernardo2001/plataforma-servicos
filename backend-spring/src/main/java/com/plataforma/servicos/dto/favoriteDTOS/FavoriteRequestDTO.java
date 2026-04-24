package com.plataforma.servicos.dto.favoriteDTOS;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.util.UUID;

@Schema(description = "Objeto para favoritar ou desfavoritar um serviço")
public record FavoriteRequestDTO(

        @Schema(description = "ID do serviço a ser favoritado",
                example = "722f9923-388e-473d-9d7a-111122223333",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Serviço é obrigatório")
        UUID serviceId

) { }
package com.plataforma.servicos.dto.favoriteDTOS;

import jakarta.validation.constraints.*;
import java.util.UUID;

public record FavoriteRequestDTO(

        @NotNull(message = "Serviço é obrigatório")
        UUID serviceId

) { }
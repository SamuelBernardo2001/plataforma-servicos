package com.plataforma.servicos.dto.favoriteDTOS;

import java.time.LocalDateTime;
import java.util.UUID;

public record FavoriteResponseDTO(

        UUID id,
        UUID userId,
        UUID serviceId,
        String serviceTitulo,
        String prestadorNome,
        LocalDateTime criadoEm

) { }
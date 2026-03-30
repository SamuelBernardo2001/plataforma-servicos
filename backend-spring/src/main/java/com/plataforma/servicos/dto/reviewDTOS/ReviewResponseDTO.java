package com.plataforma.servicos.dto.reviewDTOS;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReviewResponseDTO(

        UUID id,
        UUID serviceId,
        UUID serviceOrderId,
        UUID clienteId,
        String clienteNome,
        Integer rating,
        String comentario,
        LocalDateTime criadoEm

) { }
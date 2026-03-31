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
        // Indica se avaliação foi editada — transparência para prestador e clientes
        Boolean editado,
        // Data da edição — null se nunca foi editada
        LocalDateTime editadoEm,
        LocalDateTime criadoEm

) { }
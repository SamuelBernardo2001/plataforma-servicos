package com.plataforma.servicos.dto.reportDTOS;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReportResponseDTO(

        UUID id,
        UUID reporterId,
        String reporterNome,
        UUID reportedUserId,
        String reportedNome,
        UUID serviceOrderId,
        String motivo,
        String descricao,
        LocalDateTime criadoEm

) { }
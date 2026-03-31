package com.plataforma.servicos.dto.reportDTOS;

import com.plataforma.servicos.entity.ReportStatusEnum;

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
        // Status da denúncia — PENDENTE, RESOLVIDA ou REJEITADA
        ReportStatusEnum status,
        LocalDateTime criadoEm

) { }
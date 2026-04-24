package com.plataforma.servicos.dto.reportDTOS;

import com.plataforma.servicos.entity.ReportStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Resposta detalhada contendo os dados da denúncia para análise administrativa")
public record ReportResponseDTO(

        @Schema(description = "Identificador único da denúncia (UUID)",
                example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
        UUID id,

        @Schema(description = "ID do usuário que realizou a denúncia",
                example = "a1b2c3d4-e5f6-4372-a567-0e02b2c3d479")
        UUID reporterId,

        @Schema(description = "Nome do denunciante",
                example = "Alice Souza")
        String reporterNome,

        @Schema(description = "ID do usuário que recebeu a denúncia",
                example = "550e8400-e29b-41d4-a716-446655440000")
        UUID reportedUserId,

        @Schema(description = "Nome do usuário denunciado",
                example = "Bruno Oliveira")
        String reportedNome,

        @Schema(description = "ID da ordem de serviço vinculada (pode ser nulo se a denúncia for ao perfil)",
                example = "822f9923-388e-473d-9d7a-444455556666")
        UUID serviceOrderId,

        @Schema(description = "Título ou categoria do motivo",
                example = "Comportamento Inapropriado")
        String motivo,

        @Schema(description = "Detalhamento dos fatos denunciados",
                example = "O prestador utilizou linguagem ofensiva durante o atendimento no chat.")
        String descricao,

        @Schema(description = "Status atual da moderação",
                example = "PENDENTE")
        ReportStatusEnum status,

        @Schema(description = "Data e hora em que a denúncia foi registrada")
        LocalDateTime criadoEm

) { }
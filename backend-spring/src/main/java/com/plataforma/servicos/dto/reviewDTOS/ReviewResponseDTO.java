package com.plataforma.servicos.dto.reviewDTOS;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Resposta contendo os detalhes de uma avaliação de serviço")
public record ReviewResponseDTO(

        @Schema(description = "ID único da avaliação (UUID)",
                example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
        UUID id,

        @Schema(description = "ID do serviço que recebeu a avaliação",
                example = "722f9923-388e-473d-9d7a-111122223333")
        UUID serviceId,

        @Schema(description = "ID da ordem de serviço que originou a avaliação",
                example = "822f9923-388e-473d-9d7a-444455556666")
        UUID serviceOrderId,

        @Schema(description = "ID do cliente que realizou a avaliação",
                example = "550e8400-e29b-41d4-a716-446655440000")
        UUID clienteId,

        @Schema(description = "Nome do cliente para exibição pública",
                example = "Mariana Costa")
        String clienteNome,

        @Schema(description = "Nota dada pelo cliente (1 a 5)",
                example = "5")
        Integer rating,

        @Schema(description = "Comentário escrito pelo cliente",
                example = "Super recomendo! O serviço ficou perfeito e foi entregue antes do prazo.")
        String comentario,

        @Schema(description = "Indica se o comentário original foi alterado pelo cliente",
                example = "false")
        Boolean editado,

        @Schema(description = "Data e hora da última edição. Nulo se nunca foi editada.")
        LocalDateTime editadoEm,

        @Schema(description = "Data e hora em que a avaliação foi publicada")
        LocalDateTime criadoEm

) { }
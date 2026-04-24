package com.plataforma.servicos.dto.messageDTOS;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Resposta detalhada contendo os dados da mensagem e metadados de status")
public record MessageResponseDTO(

        @Schema(description = "Identificador único da mensagem (UUID)",
                example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
        UUID id,

        @Schema(description = "ID da ordem de serviço vinculada",
                example = "822f9923-388e-473d-9d7a-444455556666")
        UUID serviceOrderId,

        @Schema(description = "ID do usuário que enviou a mensagem",
                example = "a1b2c3d4-e5f6-4372-a567-0e02b2c3d479")
        UUID senderId,

        @Schema(description = "Nome do remetente para exibição no chat",
                example = "Carlos Silva")
        String senderNome,

        @Schema(description = "ID do usuário que recebeu a mensagem",
                example = "550e8400-e29b-41d4-a716-446655440000")
        UUID receiverId,

        @Schema(description = "Nome do destinatário para exibição no chat",
                example = "Maria Oliveira")
        String receiverNome,

        @Schema(description = "Texto da mensagem enviada",
                example = "O orçamento foi aprovado, podemos começar amanhã?")
        String conteudo,

        @Schema(description = "Indica se a mensagem já foi lida pelo destinatário",
                example = "true")
        Boolean lida,

        @Schema(description = "Indica se o conteúdo original da mensagem foi modificado",
                example = "false")
        Boolean editado,

        @Schema(description = "Data e hora da última edição. Nulo se nunca foi editada.")
        LocalDateTime editadoEm,

        @Schema(description = "Data e hora do envio original da mensagem")
        LocalDateTime enviadoEm

) { }
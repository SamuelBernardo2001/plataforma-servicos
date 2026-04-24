package com.plataforma.servicos.dto.messageDTOS;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.util.UUID;

@Schema(description = "Objeto para envio de mensagens no chat vinculado a uma ordem de serviço")
public record MessageRequestDTO(

        @Schema(description = "ID da ordem de serviço à qual a conversa pertence",
                example = "822f9923-388e-473d-9d7a-444455556666",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Ordem de serviço é obrigatória")
        UUID serviceOrderId,

        @Schema(description = "ID do usuário que receberá a mensagem",
                example = "550e8400-e29b-41d4-a716-446655440000",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Destinatário é obrigatório")
        UUID receiverId,

        @Schema(description = "Conteúdo textual da mensagem",
                example = "Olá, gostaria de confirmar o horário de chegada para amanhã.",
                minLength = 1, maxLength = 1000,
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Conteúdo é obrigatório")
        @Size(min = 1, max = 1000, message = "Mensagem deve ter entre 1 e 1000 caracteres")
        String conteudo

) { }
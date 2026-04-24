package com.plataforma.servicos.dto.reportDTOS;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.util.UUID;

@Schema(description = "Objeto para registro de denúncias contra usuários ou condutas em ordens de serviço")
public record ReportRequestDTO(

        @Schema(description = "ID do usuário que está sendo denunciado",
                example = "550e8400-e29b-41d4-a716-446655440000",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Usuário denunciado é obrigatório")
        UUID reportedUserId,

        @Schema(description = "ID da ordem de serviço onde ocorreu o incidente (opcional)",
                example = "822f9923-388e-473d-9d7a-444455556666")
        UUID serviceOrderId,

        @Schema(description = "Título resumido ou categoria do motivo da denúncia",
                example = "Comportamento Inapropriado",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Motivo é obrigatório")
        String motivo,

        @Schema(description = "Detalhamento dos fatos ocorridos (mínimo 20 caracteres)",
                example = "O prestador utilizou linguagem ofensiva durante o atendimento no chat e não compareceu.",
                minLength = 20,
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Descrição é obrigatória")
        @Size(min = 20, message = "Descrição deve ter no mínimo 20 caracteres")
        String descricao

) { }
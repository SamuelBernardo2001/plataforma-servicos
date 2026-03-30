package com.plataforma.servicos.dto.reportDTOS;

import jakarta.validation.constraints.*;
import java.util.UUID;

public record ReportRequestDTO(

        @NotNull(message = "Usuário denunciado é obrigatório")
        UUID reportedUserId,

        UUID serviceOrderId,

        @NotBlank(message = "Motivo é obrigatório")
        String motivo,

        @NotBlank(message = "Descrição é obrigatória")
        @Size(min = 20, message = "Descrição deve ter no mínimo 20 caracteres")
        String descricao

) { }
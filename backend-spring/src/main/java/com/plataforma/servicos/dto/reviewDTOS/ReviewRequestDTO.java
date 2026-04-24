package com.plataforma.servicos.dto.reviewDTOS;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.util.UUID;

@Schema(description = "Objeto para envio de avaliação e nota sobre um serviço prestado")
public record ReviewRequestDTO(

        @Schema(description = "ID da ordem de serviço concluída que será avaliada",
                example = "822f9923-388e-473d-9d7a-444455556666",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Ordem de serviço é obrigatória")
        UUID serviceOrderId,

        @Schema(description = "Nota de satisfação de 1 a 5 estrelas",
                example = "5",
                minimum = "1", maximum = "5",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Nota é obrigatória")
        @Min(value = 1, message = "Nota mínima é 1")
        @Max(value = 5, message = "Nota máxima é 5")
        Integer rating,

        @Schema(description = "Comentário detalhado sobre a experiência com o serviço",
                example = "O serviço foi excelente, o profissional foi muito pontual e organizado.",
                minLength = 10,
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Comentário é obrigatório")
        @Size(min = 10, message = "Comentário deve ter no mínimo 10 caracteres")
        String comentario

) { }
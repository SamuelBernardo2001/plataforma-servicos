package com.plataforma.servicos.dto.reviewDTOS;

import jakarta.validation.constraints.*;
import java.util.UUID;

public record ReviewRequestDTO(

        @NotNull(message = "Ordem de serviço é obrigatória")
        UUID serviceOrderId,

        @NotNull(message = "Nota é obrigatória")
        @Min(value = 1, message = "Nota mínima é 1")
        @Max(value = 5, message = "Nota máxima é 5")
        Integer rating,

        @NotBlank(message = "Comentário é obrigatório")
        @Size(min = 10, message = "Comentário deve ter no mínimo 10 caracteres")
        String comentario

) { }
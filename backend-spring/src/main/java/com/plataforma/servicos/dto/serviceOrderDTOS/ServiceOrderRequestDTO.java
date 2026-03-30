package com.plataforma.servicos.dto.serviceOrderDTOS;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

public record ServiceOrderRequestDTO(

        @NotNull(message = "Serviço é obrigatório")
        UUID serviceId,

        @NotNull(message = "Prestador é obrigatório")
        UUID prestadorId,

        @NotBlank(message = "Descrição é obrigatória")
        String descricao,

        @Positive(message = "Preço deve ser maior que zero")
        BigDecimal preco

) { }
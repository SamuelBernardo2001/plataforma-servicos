package com.plataforma.servicos.dto.serviceOrderDTOS;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Objeto para solicitação de uma nova ordem de serviço")
public record ServiceOrderRequestDTO(

        @Schema(description = "ID do serviço que está sendo contratado",
                example = "722f9923-388e-473d-9d7a-111122223333",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Serviço é obrigatório")
        UUID serviceId,

        @Schema(description = "ID do prestador que realizará o serviço",
                example = "a1b2c3d4-e5f6-4372-a567-0e02b2c3d479",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Prestador é obrigatório")
        UUID prestadorId,

        @Schema(description = "Detalhamento da solicitação do cliente ou especificações do pedido",
                example = "Preciso da instalação para sábado de manhã. O local possui fácil acesso.",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Descrição é obrigatória")
        String descricao,

        @Schema(description = "Preço negociado ou valor base do serviço",
                example = "350.00")
        @Positive(message = "Preço deve ser maior que zero")
        BigDecimal preco

) { }
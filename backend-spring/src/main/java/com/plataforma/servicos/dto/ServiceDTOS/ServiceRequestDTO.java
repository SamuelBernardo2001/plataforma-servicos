package com.plataforma.servicos.dto.ServiceDTOS;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Objeto para criação ou atualização dos dados de um serviço no catálogo")
public record ServiceRequestDTO(

        @Schema(description = "Título comercial do serviço",
                example = "Instalação de Ar Condicionado Split",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Título é obrigatório")
        String nome,

        @Schema(description = "Descrição detalhada do que está incluso no serviço",
                example = "Instalação completa até 12.000 BTUs, incluindo suporte e fiação básica.",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Descrição é obrigatória")
        String descricao,

        @Schema(description = "Valor base cobrado pelo serviço",
                example = "350.00",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Preço é obrigatório")
        @Positive(message = "Preço deve ser maior que zero")
        BigDecimal preco,

        @Schema(description = "ID da categoria onde o serviço será listado",
                example = "550e8400-e29b-41d4-a716-446655440000",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Categoria é obrigatória")
        UUID categoriaId,

        @Schema(description = "WhatsApp ou telefone para o cliente tirar dúvidas",
                example = "11999998888",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Telefone de contato é obrigatório")
        String telefoneContato

) { }
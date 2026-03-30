package com.plataforma.servicos.dto.ServiceDTOS;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

public record ServiceRequestDTO(

        @NotBlank(message = "Título é obrigatório")
        String titulo,

        @NotBlank(message = "Descrição é obrigatória")
        String descricao,

        @NotNull(message = "Preço é obrigatório")
        @Positive(message = "Preço deve ser maior que zero")
        BigDecimal preco,

        @NotNull(message = "Categoria é obrigatória")
        UUID categoriaId,

        @NotBlank(message = "Localização é obrigatória")
        String localizacao,

        @NotBlank(message = "Telefone de contato é obrigatório")
        String telefoneContato

) { }
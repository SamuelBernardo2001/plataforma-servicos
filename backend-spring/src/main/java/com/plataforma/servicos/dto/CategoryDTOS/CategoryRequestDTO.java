package com.plataforma.servicos.dto.CategoryDTOS;

import jakarta.validation.constraints.*;

public record CategoryRequestDTO(

        @NotBlank(message = "Nome da categoria é obrigatório")
        String nome,

        @NotBlank(message = "Descrição é obrigatória")
        String descricao

) { }
package com.plataforma.servicos.dto.UserDTOS;

import jakarta.validation.constraints.NotBlank;

public record UserUpdateDTO(

        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "Telefone é obrigatório")
        String telefone
) {
}

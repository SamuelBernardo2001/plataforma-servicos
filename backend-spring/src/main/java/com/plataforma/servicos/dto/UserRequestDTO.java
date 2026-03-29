package com.plataforma.servicos.dto;

import com.plataforma.servicos.entity.UserENUM;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequestDTO(

        // irei usar o @NotBlank para validar os campos para que não sejam nulos ou vazios,
        // e o @Email para validar o formato do email

        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @NotBlank(message = "O email é obrigatório")
        @Email(message = "O email deve ser válido")
        String email,

        @NotBlank(message = "A senha é obrigatória")
        String senha,

        @NotBlank(message = "O telefone é obrigatório")
        String telefone,

        @NotNull(message = "O perfil é obrigatório")
        UserENUM perfil
) {
}

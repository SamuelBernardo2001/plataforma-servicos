package com.plataforma.servicos.dto.UserDTOS;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Objeto de transferência para credenciais de acesso do usuário")
public record UserLoginDTO(

        @Schema(description = "Endereço de e-mail cadastrado",
                example = "usuario@email.com",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        String email,

        @Schema(description = "Senha de acesso do usuário (mínimo 6 caracteres)",
                example = "senha123",
                minLength = 6,
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, message = "A senha deve conter no mínimo 6 caracteres")
        String senha

) { }
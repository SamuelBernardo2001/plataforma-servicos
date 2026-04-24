package com.plataforma.servicos.dto.UserDTOS;

import com.plataforma.servicos.entity.UserENUM;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Objeto para criação de um novo usuário no sistema")
public record UserRequestDTO(

        @Schema(description = "Nome completo do usuário",
                example = "Mariana Silva",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @Schema(description = "Endereço de e-mail único para acesso",
                example = "mariana.silva@email.com",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "O email é obrigatório")
        @Email(message = "O email deve ser válido")
        String email,

        @Schema(description = "Senha de acesso (mínimo 6 caracteres)",
                example = "senha123",
                minLength = 6,
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 6, message = "A senha deve conter no mínimo 6 caracteres")
        String senha,

        @Schema(description = "Telefone de contato (preferencialmente WhatsApp)",
                example = "11988887777",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "O telefone é obrigatório")
        String telefone,

        @Schema(description = "Tipo de perfil do usuário no sistema",
                example = "CLIENTE",
                allowableValues = {"CLIENTE", "PRESTADOR"},
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "O perfil é obrigatório")
        UserENUM perfil
) {
}
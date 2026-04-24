package com.plataforma.servicos.dto.UserDTOS;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Objeto para alteração de senha de um usuário autenticado")
public record UserPasswordDTO(

        @Schema(description = "Senha atual do usuário para validação de segurança",
                example = "Antiga123#",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Senha atual é obrigatória")
        String senhaAtual,

        @Schema(description = "Nova senha desejada (mínimo 8 caracteres)",
                example = "NovaSenha2024!",
                minLength = 8,
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Nova senha é obrigatória")
        @Size(min = 8, message = "Nova senha deve ter no mínimo 8 caracteres")
        String novaSenha,

        @Schema(description = "Repetição da nova senha para evitar erros de digitação",
                example = "NovaSenha2024!",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Confirmação de senha é obrigatória")
        String confirmarSenha

) { }
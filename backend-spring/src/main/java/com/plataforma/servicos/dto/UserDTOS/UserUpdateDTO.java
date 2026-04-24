package com.plataforma.servicos.dto.UserDTOS;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Objeto para atualização de dados básicos do perfil do usuário")
public record UserUpdateDTO(

        @Schema(description = "Novo nome completo ou social",
                example = "Mariana Silva de Souza",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @Schema(description = "Novo telefone ou WhatsApp de contato",
                example = "11977776666",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Telefone é obrigatório")
        String telefone
) {
}
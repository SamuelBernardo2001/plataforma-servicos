package com.plataforma.servicos.dto.EnderecoDTOS;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Objeto de transferência para cadastro completo de endereço")
public record EnderecoRequestDTO(

        @Schema(description = "CEP do endereço", example = "50010-000", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "CEP é obrigatório")
        @Size(min = 8, max = 9, message = "CEP inválido")
        String cep,

        @Schema(description = "Rua, avenida ou praça", example = "Rua do Sol", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Logradouro é obrigatório")
        String logradouro,

        @Schema(description = "Número da residência ou estabelecimento", example = "123", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Número é obrigatório")
        String numero,

        @Schema(description = "Informações adicionais como bloco, apto ou referência", example = "Bloco B, Apto 402")
        String complemento,

        @Schema(description = "Bairro da localidade", example = "Santo Antônio", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Bairro é obrigatório")
        String bairro,

        @Schema(description = "Cidade do endereço", example = "Recife", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Cidade é obrigatória")
        String cidade,

        @Schema(description = "Sigla do estado (UF)", example = "PE", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Estado é obrigatório")
        @Size(min = 2, max = 2, message = "Estado deve ter 2 letras. Ex: PE, SP")
        String estado

) { }
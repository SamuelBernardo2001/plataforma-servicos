package com.plataforma.servicos.dto.EnderecoDTOS;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Objeto de transferência para dados de endereço simplificado do usuário")
public record UserAddressDTO(

        @Schema(description = "CEP do endereço", example = "01310-100", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "CEP é obrigatório")
        String cep,

        @Schema(description = "Rua, Avenida ou similar", example = "Avenida Paulista", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Logradouro é obrigatório")
        String logradouro,

        @Schema(description = "Número da residência", example = "1578", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Número é obrigatório")
        String numero,

        @Schema(description = "Complemento (Apto, Bloco, Casa)", example = "Apto 15")
        String complemento,

        @Schema(description = "Bairro", example = "Bela Vista", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Bairro é obrigatório")
        String bairro,

        @Schema(description = "Cidade", example = "São Paulo", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Cidade é obrigatória")
        String cidade,

        @Schema(description = "Sigla do estado (UF)", example = "SP", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Estado é obrigatório")
        @Size(min = 2, max = 2, message = "Estado deve ter 2 caracteres")
        String estado

) { }
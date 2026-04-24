package com.plataforma.servicos.dto.EnderecoDTOS;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Resposta com os dados detalhados do endereço do usuário")
public record EnderecoResponseDTO(

        @Schema(description = "Identificador único do endereço (UUID)",
                example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
        UUID id,

        @Schema(description = "CEP formatado ou apenas números", example = "50010-000")
        String cep,

        @Schema(description = "Nome da rua, avenida ou logradouro", example = "Rua do Sol")
        String logradouro,

        @Schema(description = "Número do imóvel", example = "123")
        String numero,

        @Schema(description = "Complemento (Apto, Bloco, Referência)", example = "Bloco B, Apto 402")
        String complemento,

        @Schema(description = "Bairro", example = "Santo Antônio")
        String bairro,

        @Schema(description = "Cidade", example = "Recife")
        String cidade,

        @Schema(description = "Sigla do estado", example = "PE")
        String estado,

        @Schema(description = "Data e hora em que o endereço foi cadastrado")
        LocalDateTime criadoEm,

        @Schema(description = "Data e hora da última alteração no endereço")
        LocalDateTime atualizadoEm

) { }
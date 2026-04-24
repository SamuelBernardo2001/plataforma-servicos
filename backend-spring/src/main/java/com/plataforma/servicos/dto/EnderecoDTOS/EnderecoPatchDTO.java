package com.plataforma.servicos.dto.EnderecoDTOS;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "Objeto para atualização parcial do endereço. Apenas os campos enviados serão modificados.")
public record EnderecoPatchDTO(

        @Schema(description = "CEP do endereço (apenas números ou com hífen)", example = "01001-000")
        @Size(min = 8, max = 9, message = "CEP inválido")
        String cep,

        @Schema(description = "Nome da rua, avenida ou logradouro", example = "Avenida Paulista")
        String logradouro,

        @Schema(description = "Número do imóvel", example = "1000")
        String numero,

        @Schema(description = "Complemento do endereço (Apto, Bloco, Casa)", example = "Apto 121")
        String complemento,

        @Schema(description = "Bairro da localidade", example = "Bela Vista")
        String bairro,

        @Schema(description = "Cidade do endereço", example = "São Paulo")
        String cidade,

        @Schema(description = "Sigla do estado (UF) com 2 letras", example = "SP")
        @Size(min = 2, max = 2, message = "Estado deve ter 2 letras. Ex: PE, SP")
        String estado

) { }
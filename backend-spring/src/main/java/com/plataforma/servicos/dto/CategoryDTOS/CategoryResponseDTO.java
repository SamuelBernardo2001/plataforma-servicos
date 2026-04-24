package com.plataforma.servicos.dto.CategoryDTOS;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Resposta com os dados detalhados da categoria")
public record CategoryResponseDTO(

        @Schema(description = "Identificador único da categoria (UUID)",
                example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "Nome da categoria",
                example = "Assistência Técnica")
        String nome,

        @Schema(description = "Descrição da categoria",
                example = "Reparos em eletrodomésticos, celulares e computadores")
        String descricao,

        @Schema(description = "Indica se a categoria está disponível para novos serviços",
                example = "true")
        Boolean ativo,

        @Schema(description = "Data e hora de criação do registro")
        LocalDateTime criadoEm,

        @Schema(description = "Data e hora da última atualização do registro")
        LocalDateTime atualizadoEm

) { }
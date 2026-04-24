package com.plataforma.servicos.dto.CategoryDTOS;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Objeto de transferência para criação ou atualização de uma categoria")
public record CategoryRequestDTO(

        @Schema(description = "Nome único da categoria",
                example = "Manutenção Elétrica",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Nome da categoria é obrigatório")
        String nome,

        @Schema(description = "Breve descrição sobre os serviços desta categoria",
                example = "Serviços relacionados a instalação de chuveiros, fiação e quadros de força")
        @NotBlank(message = "Descrição é obrigatória")
        String descricao

) { }
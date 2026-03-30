package com.plataforma.servicos.dto.CategoryDTOS;

import java.time.LocalDateTime;
import java.util.UUID;

public record CategoryResponseDTO(

        UUID id,
        String nome,
        String descricao,
        Boolean ativo,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm

) { }
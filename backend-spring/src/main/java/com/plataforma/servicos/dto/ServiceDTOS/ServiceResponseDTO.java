package com.plataforma.servicos.dto.ServiceDTOS;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ServiceResponseDTO(

        UUID id,
        String nome,
        String descricao,
        BigDecimal preco,
        UUID categoriaId,
        String categoriaNome,
        UUID prestadorId,
        String prestadorNome,
        String telefoneContato,
        Boolean ativo,
        Double mediaAvaliacao,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm

) { }
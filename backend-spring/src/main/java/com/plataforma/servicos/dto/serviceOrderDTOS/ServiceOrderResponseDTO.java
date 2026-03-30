package com.plataforma.servicos.dto.serviceOrderDTOS;

import com.plataforma.servicos.entity.OrderStatusEnum;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ServiceOrderResponseDTO(

        UUID id,
        UUID serviceId,
        String serviceTitulo,
        UUID clienteId,
        String clienteNome,
        UUID prestadorId,
        String prestadorNome,
        String descricao,
        BigDecimal preco,
        OrderStatusEnum status,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm,
        LocalDateTime concluidoEm

) { }
package com.plataforma.servicos.entity;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ServiceOrderModel {

    private UUID id;

    private UUID serviceId;

    private UUID clientId;

    private UUID provedorId;

    private BigDecimal price;

    @Builder.Default
    private OrderStatusEnum status = OrderStatusEnum.REQUESTED;

    private LocalDateTime criadoEm;

    private LocalDateTime atualizadoEm;

    private LocalDateTime concluidoEm;
}

package com.plataforma.servicos.entity;

import jakarta.persistence.*;
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
@Entity
@Table(name = "service_orders")
public class ServiceOrderModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID serviceId;

    @Column(nullable = false)
    private UUID clientId;

    @Column(nullable = false)
    private UUID provedorId;

    @Column(nullable = false)
    private BigDecimal price;

    @Builder.Default
    @Column(nullable = false)
    private OrderStatusEnum status = OrderStatusEnum.REQUESTED;

    private LocalDateTime criadoEm;

    private LocalDateTime atualizadoEm;

    private LocalDateTime concluidoEm;
}

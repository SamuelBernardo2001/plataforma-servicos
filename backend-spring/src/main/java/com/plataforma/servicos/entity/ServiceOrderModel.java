package com.plataforma.servicos.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "service_orders")
public class ServiceOrderModel extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Muitas ordens pertencem a um serviço
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceModel service;

    // Muitas ordens pertencem a um cliente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private UserModel cliente;

    // Muitas ordens pertencem a um prestador
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provedor_id", nullable = false)
    private UserModel prestador;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(length = 1000)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    private OrderStatusEnum status = OrderStatusEnum.REQUESTED;

    // @Version → entidade mais critica do sistema
    // Previne que cliente e prestador alterem o status
    // da mesma ordem ao mesmo tempo
    // Ex: prestador aceita enquanto cliente cancela simultaneamente
    @Version
    @Column(nullable = false)
    private Long version = 0L;

    // criadoEm e atualizadoEm → REMOVIDOS — vem do BaseEntity
    // concluidoEm → MANTIDO pois é campo de negócio, não de auditoria
    private LocalDateTime concluidoEm;

    // Uma ordem pode ter muitas mensagens
    @OneToMany(mappedBy = "serviceOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MessageModel> mensagens;

    // Uma ordem pode ter uma avaliação
    @OneToOne(mappedBy = "serviceOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ReviewModel avaliacao;

    // Uma ordem pode ter uma denúncia
    @OneToOne(mappedBy = "serviceOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ReportModel denuncia;
}
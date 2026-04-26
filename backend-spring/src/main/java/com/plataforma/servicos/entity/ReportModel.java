package com.plataforma.servicos.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "reports")
public class ReportModel extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Muitas denúncias pertencem a quem denunciou
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private UserModel reporter;

    // Muitas denúncias pertencem a quem foi denunciado
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_user_id", nullable = false)
    private UserModel reportedUser;

    // Uma denúncia pode estar vinculada a uma ordem
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_order_id")
    private ServiceOrderModel serviceOrder;

    @Column(nullable = false)
    private String razao; // Motivo da denúncia

    @Column(length = 1000)
    private String descricao;

    // Status da denúncia controlado pelo ADMIN
    // PENDENTE → aguardando análise
    // RESOLVIDA → ADMIN tomou providências
    // REJEITADA → ADMIN rejeitou por ser inválida
    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    private ReportStatusEnum status = ReportStatusEnum.PENDENTE;

}
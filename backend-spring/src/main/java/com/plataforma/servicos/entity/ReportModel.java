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
public class ReportModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID reporterId;   // Quem denunciou

    @Column(nullable = false)
    private UUID reportedUserId; // Usuário denunciado

    @Column(nullable = false)
    private UUID serviceOrderId; // Ordem relacionada (opcional)

    @Column(nullable = false)
    private String razao; // Motivo da denúncia

    @Column(length = 1000)
    private String descricao;

    private LocalDateTime criadoEm;
}

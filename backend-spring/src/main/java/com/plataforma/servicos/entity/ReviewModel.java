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
@Table(name = "reviews")
public class ReviewModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Muitas avaliações pertencem a um serviço
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceModel service;

    // Muitas avaliações pertencem a um usuário (cliente)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel usuario;

    // Uma avaliação pertence a uma ordem (só existe após COMPLETED)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_order_id", nullable = false, unique = true)
    private ServiceOrderModel serviceOrder;

    @Column(nullable = false)
    private Integer classificacao; // Ex: 1 a 5

    @Column(length = 1000)
    private String comentario;

    // Indica se a avaliação foi editada pelo cliente
    // Mostra transparência para o prestador e outros clientes
    @Builder.Default
    private Boolean editado = false;

    // Data da última edição da avaliação
    // Null se nunca foi editada
    private LocalDateTime editadoEm;

    private LocalDateTime criadoEm;
}
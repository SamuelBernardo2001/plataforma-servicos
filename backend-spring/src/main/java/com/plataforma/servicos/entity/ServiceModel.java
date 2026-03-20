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
@Table(name = "services")
public class ServiceModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(length = 1000)
    private String descricao;

    @Column(nullable = false)
    private BigDecimal preco;

    @Column(nullable = false)
    private UserModel prestador; // Prestador do serviço

    @Column(nullable = false)
    private CategoryModel categoria;

    @Builder.Default
    @Column(nullable = false)
    private Boolean ativo = true;

    private LocalDateTime criadoEm;

     private LocalDateTime atualizadoEm;}

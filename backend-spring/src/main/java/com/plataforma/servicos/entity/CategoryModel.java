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
@Table(name = "categories")
public class CategoryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String nome; // (Ex: Elétrica, Encanamento)

    @Column(length = 1000)
    private String descricao;

    @Builder.Default
    private Boolean ativo = true;

    private LocalDateTime criadoEm;

    private LocalDateTime atualizadoEm;
}
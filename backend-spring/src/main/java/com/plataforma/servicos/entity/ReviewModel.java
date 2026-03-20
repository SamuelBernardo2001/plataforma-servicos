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

    @Column(nullable = false)
    private UUID serviceId;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private Integer classificacao; // Ex: 1 a 5

    @Column(length = 1000)
    private String comentario;

    private LocalDateTime criadoEm;
}

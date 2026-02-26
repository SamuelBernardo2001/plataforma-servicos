package com.plataforma.servicos.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReviewModel {

    private UUID id;

    private UUID serviceId;

    private UUID userId;

    private Integer classificacao; // Ex: 1 a 5

    private String comentario;

    private LocalDateTime criadoEm;
}

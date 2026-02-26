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
public class CategoryModel {

    private UUID id;

    private String nome; // (Ex: Elétrica, Encanamento)

    private String descricao;

    @Builder.Default
    private Boolean ativo = true;

    private LocalDateTime criadoEm;

    private LocalDateTime atualizadoEm;
}
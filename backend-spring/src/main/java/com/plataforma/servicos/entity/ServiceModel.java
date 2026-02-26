package com.plataforma.servicos.entity;

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
public class ServiceModel {

    private UUID id;

    private String nome;

    private String descricao;

    private BigDecimal preco;

    private UserModel prestador; // Prestador do serviço

    private CategoryModel categoria;
    @Builder.Default
    private Boolean ativo = true;

    private LocalDateTime criadoEm;

     private LocalDateTime atualizadoEm;}

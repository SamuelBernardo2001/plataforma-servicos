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
public class ReportModel {

    private UUID id;

    private UUID reporterId;   // Quem denunciou

    private UUID reportedUserId; // Usuário denunciado

    private UUID serviceOrderId; // Ordem relacionada (opcional)

    private String razao; // Motivo da denúncia

    private String descricao;

    private LocalDateTime criadoEm;
}

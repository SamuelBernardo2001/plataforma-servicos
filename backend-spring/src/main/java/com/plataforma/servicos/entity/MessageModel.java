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
public class MessageModel {

    private UUID id;

    private UUID serviceOrderId;

    private UUID remetenteId;

    private UUID receptorId;

    private String conteudo;

    private Boolean ler;

    private LocalDateTime enviadoEm;
}

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
@Table(name = "messages")
public class MessageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID serviceOrderId;

    @Column(nullable = false)
    private UUID remetenteId;

    @Column(nullable = false)
    private UUID receptorId;

    @Column(nullable = false, columnDefinition = "TEXT") // Permite mensagens longas
    private String conteudo;

    @Column(nullable = false)
    private Boolean ler;

    private LocalDateTime enviadoEm;
}

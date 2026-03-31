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

    // Muitas mensagens pertencem a uma ordem
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_order_id", nullable = false)
    private ServiceOrderModel serviceOrder;

    // Muitas mensagens pertencem a quem enviou
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "remetente_id", nullable = false)
    private UserModel remetente;

    // Muitas mensagens pertencem a quem recebe
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receptor_id", nullable = false)
    private UserModel receptor;

    @Column(nullable = false, columnDefinition = "TEXT") // Permite mensagens longas
    private String conteudo;

    @Column(nullable = false)
    private Boolean ler;

    // Indica se a mensagem foi editada pelo remetente
    // Mostra "editado" ao lado da mensagem no frontend se true
    @Builder.Default
    private Boolean editado = false;

    // Data e hora da última edição da mensagem
    // Null se a mensagem nunca foi editada
    private LocalDateTime editadoEm;

    private LocalDateTime enviadoEm;
}
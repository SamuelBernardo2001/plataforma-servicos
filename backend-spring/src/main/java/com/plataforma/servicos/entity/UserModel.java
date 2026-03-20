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
@Table(name = "users")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Builder.Default // Define o perfil padrão como CLIENTE
    private UserENUM perfil = UserENUM.CLIENTE; // CLIENTE, PRESTADOR, ADMIN

    private String telefone;

    @Builder.Default
    private Boolean ativo = true;

    private LocalDateTime criadoEm;

    private LocalDateTime atualizadoEm;
}

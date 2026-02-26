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
public class UserModel {

    private UUID id;

    private String nome;

    private String email;

    private String senha;

    @Builder.Default // Define o perfil padrão como CLIENTE
    private UserENUM perfil = UserENUM.CLIENTE; // CLIENTE, PRESTADOR, ADMIN

    private String telefone;

    @Builder.Default
    private Boolean ativo = true;

    private LocalDateTime criadoEm;

    private LocalDateTime atualizadoEm;
}

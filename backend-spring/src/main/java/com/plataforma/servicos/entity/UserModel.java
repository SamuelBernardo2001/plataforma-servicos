package com.plataforma.servicos.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "users")
public class UserModel extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default // Define o perfil padrão como CLIENTE
    private UserENUM perfil = UserENUM.CLIENTE; // CLIENTE, PRESTADOR, ADMIN

    private String telefone;

    @Builder.Default
    private Boolean ativo = true;

    // Um usuário pode ter muitos serviços (prestador)
    @OneToMany(mappedBy = "prestador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ServiceModel> servicos;

    // Um usuário pode ter muitas ordens como cliente
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ServiceOrderModel> ordensComoCliente;

    // Um usuário pode ter muitas ordens como prestador
    @OneToMany(mappedBy = "prestador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ServiceOrderModel> ordensComoPrestador;

    // Um usuário pode ter muitos favoritos
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FavoriteModel> favoritos;

    // Um usuário pode ter muitas denúncias feitas
    @OneToMany(mappedBy = "reporter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReportModel> denuncias;

    // Um usuário tem um endereço
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private EnderecoModel endereco;
}
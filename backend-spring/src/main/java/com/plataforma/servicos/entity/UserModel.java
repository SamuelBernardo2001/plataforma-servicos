package com.plataforma.servicos.entity;

import com.plataforma.servicos.util.SoftDeletable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

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
// @Where → filtro automatico aplicado em TODA query do Hibernate
// para esta entidade — nunca retorna usuarios desativados
// sem precisar de filtro manual nos Services
@Where(clause = "ativo = true")
public class UserModel extends BaseEntity implements SoftDeletable {

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

    // Campo de soft delete — false = usuario desativado
    // @Where garante que ativo = false nunca aparece nas queries
    @Builder.Default
    @Column(nullable = false)
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
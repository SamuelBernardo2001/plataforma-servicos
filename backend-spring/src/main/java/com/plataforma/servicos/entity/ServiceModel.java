package com.plataforma.servicos.entity;

import com.plataforma.servicos.util.SoftDeletable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
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
@Table(name = "services")
// @Where → garante que servicos desativados nunca aparecem
// nas listagens publicas nem nos relacionamentos @OneToMany
// ex: user.getServicos() nunca retorna servicos inativos
@Where(clause = "ativo = true")
public class ServiceModel extends BaseEntity implements SoftDeletable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(length = 1000)
    private String descricao;

    @Column(nullable = false)
    private BigDecimal preco;

    @Column(length = 20)
    private String telefoneContato;

    // Muitos serviços pertencem a um prestador
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prestador_id", nullable = false)
    private UserModel prestador;

    // Muitos serviços pertencem a uma categoria
    @ManyToOne(fetch = FetchType.EAGER) // Em vez de LAZY
    @JoinColumn(name = "categoria_id")
    private CategoryModel categoria;

    // Campo de soft delete — false = servico desativado
    @Builder.Default
    @Column(nullable = false)
    private Boolean ativo = true;

    // Um serviço pode ter muitas imagens
    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ServiceImageModel> imagens;

    // Um serviço pode ter muitas ordens
    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ServiceOrderModel> ordens;

    // Um serviço pode ter muitas avaliações
    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReviewModel> avaliacoes;

    // Um serviço pode ser favoritado por muitos usuários
    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FavoriteModel> favoritos;
}
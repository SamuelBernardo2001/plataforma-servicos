package com.plataforma.servicos.entity;

import com.plataforma.servicos.util.SoftDeletable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

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
@Table(name = "categories")
// @Where → garante que categorias desativadas nao aparecem
// nas listagens nem ao buscar servicos por categoria
// Importante: servicos que usavam categoria desativada
// ficam pendentes — prestador precisa atualizar
@Where(clause = "ativo = true")
public class CategoryModel extends BaseEntity implements SoftDeletable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String nome; // (Ex: Elétrica, Encanamento)

    @Column(length = 1000)
    private String descricao;

    // Campo de soft delete — false = categoria desativada
    @Builder.Default
    @Column(nullable = false)
    private Boolean ativo = true;

    // Uma categoria pode ter muitos serviços
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ServiceModel> servicos;
}
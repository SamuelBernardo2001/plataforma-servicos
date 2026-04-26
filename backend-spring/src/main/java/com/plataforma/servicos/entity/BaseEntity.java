package com.plataforma.servicos.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

// @MappedSuperclass → indica que essa classe é uma superclasse mapeada
// Seus campos são herdados pelas entidades filhas como se fossem dela
// Não cria tabela própria no banco — os campos vão para cada tabela filha
@MappedSuperclass

// @EntityListeners → registra o listener do Spring que preenche
// os campos de auditoria automaticamente antes de cada INSERT e UPDATE
@EntityListeners(AuditingEntityListener.class)

// @Getter do Lombok → gera getters para todos os campos
// Não precisamos de @Setter — campos de auditoria não devem ser alterados manualmente
@Getter
public abstract class BaseEntity {

    // @CreatedDate → preenchido automaticamente pelo Spring no INSERT
    // updatable = false → nunca é alterado após a criação
    // nullable = false → sempre preenchido
    @CreatedDate
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    // @LastModifiedDate → atualizado automaticamente pelo Spring no UPDATE
    // Preenchido também no INSERT com o mesmo valor do criadoEm
    @LastModifiedDate
    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    // @CreatedBy → preenchido automaticamente pelo AuditorAwareImpl
    // Por enquanto retorna "SYSTEM" pois JWT ainda não foi implementado
    // No M6 será o email do usuário autenticado via token JWT
    // updatable = false → nunca é alterado após a criação
    @CreatedBy
    @Column(name = "criado_por", nullable = false, updatable = false, length = 100)
    private String criadoPor;

    // @LastModifiedBy → atualizado automaticamente pelo AuditorAwareImpl
    // Por enquanto retorna "SYSTEM"
    // No M6 será o email do usuário autenticado via token JWT
    @LastModifiedBy
    @Column(name = "atualizado_por", nullable = false, length = 100)
    private String atualizadoPor;
}
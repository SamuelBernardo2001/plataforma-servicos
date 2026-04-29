package com.plataforma.servicos.util;

// Interface marcadora para entidades que implementam Soft Delete
// Soft Delete = ao "deletar" apenas marca ativo = false
// Os dados nunca sao apagados fisicamente do banco
//
// Por que isso importa?
// Se um prestador for removido, suas ordens historicas,
// avaliacoes e mensagens ainda precisam existir para auditoria
// e integridade referencial do banco de dados
//
// Entidades que implementam: UserModel, ServiceModel, CategoryModel
public interface SoftDeletable {

    // Retorna se a entidade esta ativa
    // Implementado automaticamente pelo Lombok @Getter nas entidades
    Boolean getAtivo();

    // Desativa a entidade — chamado no Service ao fazer soft delete
    // Implementado automaticamente pelo Lombok @Setter nas entidades
    void setAtivo(Boolean ativo);
}
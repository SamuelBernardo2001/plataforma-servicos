package com.plataforma.servicos.entity;

public enum ReportStatusEnum {

    // Denúncia criada e aguardando análise do ADMIN
    PENDENTE,

    // ADMIN analisou e resolveu a denúncia (ex: usuário banido, conteúdo removido)
    RESOLVIDA,

    // ADMIN analisou e rejeitou a denúncia por ser inválida ou sem fundamento
    REJEITADA
}
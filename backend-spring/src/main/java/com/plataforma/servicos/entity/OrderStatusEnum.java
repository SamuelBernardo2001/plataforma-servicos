package com.plataforma.servicos.entity;

public enum OrderStatusEnum {

    REQUESTED, // Pedido solicitado, aguardando aceitação do prestador de serviço
    ACCEPTED,  // Pedido aceito pelo prestador de serviço
    COMPLETED, // Pedido concluído
    CANCELED   // Pedido cancelado
}

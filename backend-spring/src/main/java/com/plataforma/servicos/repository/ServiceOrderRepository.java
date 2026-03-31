package com.plataforma.servicos.repository;

import com.plataforma.servicos.entity.OrderStatusEnum;
import com.plataforma.servicos.entity.ServiceOrderModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ServiceOrderRepository extends JpaRepository<ServiceOrderModel, UUID> {

    // Busca ordens do cliente — usado no painel do cliente
    List<ServiceOrderModel> findByClienteId(UUID clienteId);

    // Busca ordens do prestador — usado no painel do prestador
    List<ServiceOrderModel> findByPrestadorId(UUID prestadorId);

    // Busca ordens do cliente por status — usado na listagem filtrada
    List<ServiceOrderModel> findByClienteIdAndStatus(UUID clienteId, OrderStatusEnum status);

    // Busca ordens do prestador por status — usado na listagem filtrada
    List<ServiceOrderModel> findByPrestadorIdAndStatus(UUID prestadorId, OrderStatusEnum status);

    // Busca ordens de um cliente para um serviço específico
    // Usado para validar ordem ativa e liberar avaliação
    List<ServiceOrderModel> findByClienteIdAndServiceId(UUID clienteId, UUID serviceId);
}
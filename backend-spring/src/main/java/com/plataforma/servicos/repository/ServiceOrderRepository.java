package com.plataforma.servicos.repository;

import com.plataforma.servicos.entity.OrderStatusEnum;
import com.plataforma.servicos.entity.ServiceOrderModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ServiceOrderRepository extends JpaRepository<ServiceOrderModel, UUID> {

    // Ordens do cliente com paginação
    // Busca ordens do cliente — usado no painel do cliente
    Page<ServiceOrderModel> findByClienteId(UUID clienteId, Pageable pageable);

    // Ordens do prestador com paginação
    // Busca ordens do prestador — usado no painel do prestador
    Page<ServiceOrderModel> findByPrestadorId(UUID prestadorId, Pageable pageable);

    // Ordens do cliente por status com paginação
    // Busca ordens do cliente por status — usado na listagem filtrada
    Page<ServiceOrderModel> findByClienteIdAndStatus(
            UUID clienteId, OrderStatusEnum status, Pageable pageable);

    // Ordens do prestador por status com paginação
    // Busca ordens do prestador por status — usado na listagem filtrada
    Page<ServiceOrderModel> findByPrestadorIdAndStatus(
            UUID prestadorId, OrderStatusEnum status, Pageable pageable);

    // Busca ordens de um cliente para um serviço específico
    // Usado para validar ordem ativa e liberar avaliação
    // Sem paginação — usado para validações internas no Service
    List<ServiceOrderModel> findByClienteIdAndServiceId(UUID clienteId, UUID serviceId);

    // Busca ordens entre um cliente e um prestador específico
    // Usado no EnderecoService para validar se prestador pode ver endereço do cliente
    // Sem paginação — usado no EnderecoService para validar acesso
    List<ServiceOrderModel> findByClienteIdAndPrestadorId(UUID clienteId, UUID prestadorId);
}
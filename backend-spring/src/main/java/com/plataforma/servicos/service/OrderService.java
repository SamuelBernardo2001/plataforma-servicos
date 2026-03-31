package com.plataforma.servicos.service;

import com.plataforma.servicos.dto.serviceOrderDTOS.ServiceOrderRequestDTO;
import com.plataforma.servicos.dto.serviceOrderDTOS.ServiceOrderResponseDTO;
import com.plataforma.servicos.entity.*;
import com.plataforma.servicos.mapper.ServiceOrderMapper;
import com.plataforma.servicos.repository.ServiceOrderRepository;
import com.plataforma.servicos.repository.ServiceRepository;
import com.plataforma.servicos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ServiceOrderRepository serviceOrderRepository;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final ServiceOrderMapper serviceOrderMapper;

    // Busca ordem por ID
    // Regra: apenas cliente ou prestador da ordem podem visualizar
    public ServiceOrderResponseDTO findById(UUID id, UUID usuarioId) {
        ServiceOrderModel order = serviceOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ordem não encontrada"));

        if (!order.getCliente().getId().equals(usuarioId) &&
                !order.getPrestador().getId().equals(usuarioId)) {
            throw new RuntimeException("Você não tem permissão para visualizar esta ordem");
        }

        return serviceOrderMapper.toResponseDTO(order);
    }

    // Lista ordens do cliente
    // Regra: cliente só vê suas próprias ordens
    public List<ServiceOrderResponseDTO> findByCliente(UUID clienteId) {
        userRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        return serviceOrderRepository.findByClienteId(clienteId)
                .stream()
                .map(serviceOrderMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Lista ordens recebidas pelo prestador
    // Regra: prestador só vê suas próprias ordens recebidas
    public List<ServiceOrderResponseDTO> findByPrestador(UUID prestadorId) {
        userRepository.findById(prestadorId)
                .orElseThrow(() -> new RuntimeException("Prestador não encontrado"));

        return serviceOrderRepository.findByPrestadorId(prestadorId)
                .stream()
                .map(serviceOrderMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Lista ordens por status
    // Regra: apenas o próprio usuário pode ver suas ordens por status
    public List<ServiceOrderResponseDTO> findByStatus(UUID usuarioId, OrderStatusEnum status) {
        return serviceOrderRepository.findByClienteIdAndStatus(usuarioId, status)
                .stream()
                .map(serviceOrderMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Cria nova ordem de serviço (contratação)
    // Regra: apenas CLIENTE pode criar ordem
    // Regra: serviço deve estar ativo
    // Regra: cliente não pode contratar seu próprio serviço
    // Regra: cliente não pode ter ordem REQUESTED ou ACCEPTED para o mesmo serviço
    @Transactional
    public ServiceOrderResponseDTO create(UUID clienteId, ServiceOrderRequestDTO dto) {
        UserModel cliente = userRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        if (!UserENUM.CLIENTE.equals(cliente.getPerfil())) {
            throw new RuntimeException("Apenas clientes podem criar ordens de serviço");
        }

        ServiceModel service = serviceRepository.findById(dto.serviceId())
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        if (Boolean.FALSE.equals(service.getAtivo())) {
            throw new RuntimeException("Serviço não está disponível");
        }

        if (service.getPrestador().getId().equals(clienteId)) {
            throw new RuntimeException("Você não pode contratar seu próprio serviço");
        }

        // Verifica se já existe ordem ativa para esse serviço
        boolean ordemAtiva = serviceOrderRepository
                .findByClienteIdAndServiceId(clienteId, dto.serviceId())
                .stream()
                .anyMatch(o -> o.getStatus().equals(OrderStatusEnum.REQUESTED) ||
                        o.getStatus().equals(OrderStatusEnum.ACCEPTED));

        if (ordemAtiva) {
            throw new RuntimeException("Você já possui uma ordem ativa para este serviço");
        }

        ServiceOrderModel order = serviceOrderMapper.toModel(dto);
        order.setCliente(cliente);
        order.setPrestador(service.getPrestador());
        order.setService(service);
        order.setStatus(OrderStatusEnum.REQUESTED);
        order.setCriadoEm(LocalDateTime.now());
        order.setAtualizadoEm(LocalDateTime.now());

        return serviceOrderMapper.toResponseDTO(serviceOrderRepository.save(order));
    }

    // Atualiza status da ordem
    // Regra: apenas o prestador pode ACCEPTED ou CANCELED (quando REQUESTED)
    // Regra: apenas o prestador pode marcar como COMPLETED (quando ACCEPTED)
    // Regra: cliente pode CANCELED apenas quando REQUESTED
    // Regra: ordem COMPLETED ou CANCELED não pode mudar de status
    @Transactional
    public ServiceOrderResponseDTO updateStatus(UUID id, UUID usuarioId, OrderStatusEnum novoStatus) {
        ServiceOrderModel order = serviceOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ordem não encontrada"));

        if (OrderStatusEnum.COMPLETED.equals(order.getStatus()) ||
                OrderStatusEnum.CANCELED.equals(order.getStatus())) {
            throw new RuntimeException("Ordem já finalizada não pode ter status alterado");
        }

        boolean isPrestador = order.getPrestador().getId().equals(usuarioId);
        boolean isCliente = order.getCliente().getId().equals(usuarioId);

        // Prestador pode aceitar ou cancelar ordem REQUESTED
        if (isPrestador && OrderStatusEnum.REQUESTED.equals(order.getStatus())) {
            if (!OrderStatusEnum.ACCEPTED.equals(novoStatus) &&
                    !OrderStatusEnum.CANCELED.equals(novoStatus)) {
                throw new RuntimeException("Prestador só pode ACEITAR ou CANCELAR ordem solicitada");
            }
        }

        // Prestador pode completar ordem ACCEPTED
        else if (isPrestador && OrderStatusEnum.ACCEPTED.equals(order.getStatus())) {
            if (!OrderStatusEnum.COMPLETED.equals(novoStatus)) {
                throw new RuntimeException("Prestador só pode COMPLETAR ordem aceita");
            }
            order.setConcluidoEm(LocalDateTime.now());
        }

        // Cliente pode cancelar apenas ordem REQUESTED
        else if (isCliente && OrderStatusEnum.REQUESTED.equals(order.getStatus())) {
            if (!OrderStatusEnum.CANCELED.equals(novoStatus)) {
                throw new RuntimeException("Cliente só pode CANCELAR ordem solicitada");
            }
        }

        else {
            throw new RuntimeException("Você não tem permissão para alterar o status desta ordem");
        }

        order.setStatus(novoStatus);
        order.setAtualizadoEm(LocalDateTime.now());

        return serviceOrderMapper.toResponseDTO(serviceOrderRepository.save(order));
    }

}
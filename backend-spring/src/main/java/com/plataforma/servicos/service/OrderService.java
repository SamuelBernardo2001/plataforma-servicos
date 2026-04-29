package com.plataforma.servicos.service;

import com.plataforma.servicos.dto.serviceOrderDTOS.ServiceOrderRequestDTO;
import com.plataforma.servicos.dto.serviceOrderDTOS.ServiceOrderResponseDTO;
import com.plataforma.servicos.entity.*;
import com.plataforma.servicos.mapper.ServiceOrderMapper;
import com.plataforma.servicos.repository.ServiceOrderRepository;
import com.plataforma.servicos.repository.ServiceRepository;
import com.plataforma.servicos.repository.UserRepository;
import com.plataforma.servicos.util.PaginatedResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final ServiceOrderRepository serviceOrderRepository;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final ServiceOrderMapper serviceOrderMapper;
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

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

    // Regra: cliente só vê suas próprias ordens
    // Lista ordens do cliente com paginação
    public PaginatedResponse<ServiceOrderResponseDTO> findByCliente(
            UUID clienteId, Pageable pageable) {
        userRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Page<ServiceOrderResponseDTO> page = serviceOrderRepository
                .findByClienteId(clienteId, pageable)
                .map(serviceOrderMapper::toResponseDTO);
        return PaginatedResponse.of(page);
    }

    // Regra: prestador só vê suas próprias ordens recebidas
    // Lista ordens do prestador com paginação
    public PaginatedResponse<ServiceOrderResponseDTO> findByPrestador(
            UUID prestadorId, Pageable pageable) {
        userRepository.findById(prestadorId)
                .orElseThrow(() -> new RuntimeException("Prestador não encontrado"));

        Page<ServiceOrderResponseDTO> page = serviceOrderRepository
                .findByPrestadorId(prestadorId, pageable)
                .map(serviceOrderMapper::toResponseDTO);
        return PaginatedResponse.of(page);
    }

    // Regra: apenas o próprio usuário pode ver suas ordens por status
    // Lista ordens por status com paginação
    public PaginatedResponse<ServiceOrderResponseDTO> findByStatus(
            UUID usuarioId, OrderStatusEnum status, Pageable pageable) {
        Page<ServiceOrderResponseDTO> page = serviceOrderRepository
                .findByClienteIdAndStatus(usuarioId, status, pageable)
                .map(serviceOrderMapper::toResponseDTO);
        return PaginatedResponse.of(page);
    }

    /**
     * Realiza a abertura de uma nova Ordem de Serviço (contratação).
     * * O processo segue este fluxo:
     * 1. Verificação de Identidade: Garante que o solicitante existe e possui perfil de CLIENTE.
     * 2. Disponibilidade do Serviço: Verifica se o serviço existe e se está ativo para novas ordens.
     * 3. Regra de Auto-Contratação: Impede que um prestador contrate seus próprios serviços.
     * 4. Controle de Duplicidade: Verifica no histórico se já existe uma solicitação em andamento
     * (REQUESTED ou ACCEPTED) para evitar ordens duplicadas.
     * 5. Montagem do Vínculo: Associa o Cliente, o Prestador (vindo do serviço) e o Serviço à Ordem.
     * 6. Persistência e Auditoria: Salva a ordem com status inicial REQUESTED, acionando os
     * campos de auditoria automáticos da migration V14.
     */
    @Transactional
    public ServiceOrderResponseDTO create(UUID clienteId, ServiceOrderRequestDTO dto) {
        log.info("Cliente {} tentando criar ordem para servico {}", clienteId, dto.serviceId());

        // 1. Valida existência e perfil do Cliente
        UserModel cliente = userRepository.findById(clienteId)
                .orElseThrow(() -> {
                    log.warn("Falha ao criar ordem: Cliente {} nao encontrado", clienteId);
                    return new RuntimeException("Cliente não encontrado");
                });

        if (!UserENUM.CLIENTE.equals(cliente.getPerfil())) {
            log.warn("Tentativa negada: Usuario {} nao e um cliente", clienteId);
            throw new RuntimeException("Apenas clientes podem criar ordens de serviço");
        }

        // 2. Valida existência e status do Serviço
        ServiceModel service = serviceRepository.findById(dto.serviceId())
                .orElseThrow(() -> {
                    log.warn("Falha ao criar ordem: Servico {} nao encontrado", dto.serviceId());
                    return new RuntimeException("Serviço não encontrado");
                });

        if (Boolean.FALSE.equals(service.getAtivo())) {
            log.warn("Falha ao criar ordem: Servico {} esta inativo", dto.serviceId());
            throw new RuntimeException("Serviço não está disponível");
        }

        // 3. Impede auto-contratação
        if (service.getPrestador().getId().equals(clienteId)) {
            log.warn("Tentativa negada: Cliente {} tentou contratar o proprio servico {}", clienteId, service.getId());
            throw new RuntimeException("Você não pode contratar seu próprio serviço");
        }

        // 4. Verifica se já existe ordem ativa (Evita spam/duplicidade)
        boolean ordemAtiva = serviceOrderRepository
                .findByClienteIdAndServiceId(clienteId, dto.serviceId())
                .stream()
                .anyMatch(o -> o.getStatus().equals(OrderStatusEnum.REQUESTED) ||
                        o.getStatus().equals(OrderStatusEnum.ACCEPTED));

        if (ordemAtiva) {
            log.warn("Cliente {} ja possui ordem ativa para o servico {}", clienteId, service.getId());
            throw new RuntimeException("Você já possui uma ordem ativa para este serviço");
        }

        // 5. Preparação do Modelo
        ServiceOrderModel order = serviceOrderMapper.toModel(dto);
        order.setCliente(cliente);
        order.setPrestador(service.getPrestador()); // Vincula o dono do serviço como prestador da ordem
        order.setService(service);
        order.setStatus(OrderStatusEnum.REQUESTED);

        // 6. Persistência
        ServiceOrderResponseDTO response = serviceOrderMapper.toResponseDTO(serviceOrderRepository.save(order));

        log.info("Ordem criada com sucesso — id: {}, status: REQUESTED", response.id());
        return response;
    }

    /**
     * Gerencia a transição de estados de uma Ordem de Serviço.
     * * O processo segue este fluxo:
     * 1. Recuperação e Proteção de Estado Final: Busca a ordem e impede qualquer alteração
     * se ela já estiver em um estado terminal (COMPLETED ou CANCELED).
     * 2. Identificação de Papéis: Determina se o usuário que solicita a alteração é o
     * Cliente ou o Prestador vinculado àquela ordem específica.
     * 3. Validação de Regras de Transição:
     * - Prestador: Gere o ciclo de vida (Aceitar/Cancelar quando solicitado, Concluir quando aceito).
     * - Cliente: Possui permissão limitada para desistência (Cancelar apenas antes do aceite).
     * 4. Registro de Negócio: Caso a ordem seja concluída, registra o momento exato da entrega
     * no campo 'concluidoEm'.
     * 5. Persistência e Auditoria: Salva o novo status, disparando a atualização automática
     * das colunas 'atualizado_em' e 'atualizado_por' (BaseEntity/V14).
     */
    @Transactional
    public ServiceOrderResponseDTO updateStatus(UUID id, UUID usuarioId, OrderStatusEnum novoStatus) {
        log.info("Atualizando status da ordem {} para {} — solicitado por usuario {}", id, novoStatus, usuarioId);

        ServiceOrderModel order = serviceOrderRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Falha na atualizacao: Ordem {} nao encontrada", id);
                    return new RuntimeException("Ordem não encontrada");
                });

        // 1. Regra de Imutabilidade de Estados Finais
        if (OrderStatusEnum.COMPLETED.equals(order.getStatus()) ||
                OrderStatusEnum.CANCELED.equals(order.getStatus())) {
            log.warn("Tentativa negada: Ordem {} ja esta finalizada como {}", id, order.getStatus());
            throw new RuntimeException("Ordem já finalizada não pode ter status alterado");
        }

        boolean isPrestador = order.getPrestador().getId().equals(usuarioId);
        boolean isCliente = order.getCliente().getId().equals(usuarioId);

        // 2. Validação de Permissões e Transições Lógicas

        // Fluxo do PRESTADOR quando a ordem é solicitada (REQUESTED)
        if (isPrestador && OrderStatusEnum.REQUESTED.equals(order.getStatus())) {
            if (!OrderStatusEnum.ACCEPTED.equals(novoStatus) &&
                    !OrderStatusEnum.CANCELED.equals(novoStatus)) {
                throw new RuntimeException("Prestador só pode ACEITAR ou CANCELAR ordem solicitada");
            }
        }

        // Fluxo do PRESTADOR quando a ordem já foi aceita (ACCEPTED)
        else if (isPrestador && OrderStatusEnum.ACCEPTED.equals(order.getStatus())) {
            if (!OrderStatusEnum.COMPLETED.equals(novoStatus)) {
                throw new RuntimeException("Prestador só pode COMPLETAR ordem aceita");
            }
            // Regra de Negócio: Data de conclusão real da entrega
            order.setConcluidoEm(LocalDateTime.now());
        }

        // Fluxo do CLIENTE (Só cancela antes do prestador aceitar)
        else if (isCliente && OrderStatusEnum.REQUESTED.equals(order.getStatus())) {
            if (!OrderStatusEnum.CANCELED.equals(novoStatus)) {
                throw new RuntimeException("Cliente só pode CANCELAR ordem solicitada");
            }
        }

        // Bloqueio de Segurança: Usuário não pertence à ordem ou transição inválida
        else {
            log.error("Acesso negado: Usuario {} tentou alterar ordem {} sem permissao", usuarioId, id);
            throw new RuntimeException("Você não tem permissão para alterar o status desta ordem");
        }

        // 3. Aplicação da Mudança
        order.setStatus(novoStatus);
        ServiceOrderResponseDTO response = serviceOrderMapper.toResponseDTO(serviceOrderRepository.save(order));

        log.info("Status da ordem {} atualizado para {} com sucesso", id, novoStatus);
        return response;
    }

    // Verifica se existe ordem COMPLETED entre cliente e serviço
    // Usado pelo ReviewService para validar se cliente pode avaliar
    public boolean existeOrdemConcluida(UUID clienteId, UUID serviceId) {
        return serviceOrderRepository
                .findByClienteIdAndServiceId(clienteId, serviceId)
                .stream()
                .anyMatch(o -> OrderStatusEnum.COMPLETED.equals(o.getStatus()));
    }

}
package com.plataforma.servicos.service;

import com.plataforma.servicos.dto.reviewDTOS.ReviewRequestDTO;
import com.plataforma.servicos.dto.reviewDTOS.ReviewResponseDTO;
import com.plataforma.servicos.entity.*;
import com.plataforma.servicos.mapper.ReviewMapper;
import com.plataforma.servicos.repository.ReviewRepository;
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

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ServiceOrderRepository serviceOrderRepository;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;
    private static final Logger log = LoggerFactory.getLogger(ReviewService.class);

    // Regra: qualquer pessoa pode ver as avaliações — são públicas
    // Regra: serviço deve existir
    // Lista avaliações de um serviço com paginação
    public PaginatedResponse<ReviewResponseDTO> findByService(
            UUID serviceId, Pageable pageable) {
        serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        Page<ReviewResponseDTO> page = reviewRepository
                .findByServiceId(serviceId, pageable)
                .map(reviewMapper::toResponseDTO);
        return PaginatedResponse.of(page);
    }

    /**
     * Registra a avaliação de um serviço após sua conclusão.
     * * O processo segue este fluxo:
     * 1. Verificação de Identidade: Garante que o autor é um CLIENTE real no sistema.
     * 2. Vínculo de Propriedade: Valida se a Ordem de Serviço (OS) informada pertence
     * ao cliente que está tentando avaliar, impedindo avaliações fraudulentas.
     * 3. Validação de Experiência Real: Exige que o status da ordem seja COMPLETED.
     * Isso evita avaliações de serviços cancelados ou ainda não executados.
     * 4. Proteção contra Spam: Verifica se o cliente já avaliou este serviço específico
     * anteriormente, mantendo a integridade da média de notas.
     * 5. Registro e Auditoria: Associa a avaliação ao Serviço, ao Cliente e à OS.
     * O BaseEntity preencherá automaticamente 'criado_em' via auditoria do Spring Data JPA.
     */
    @Transactional
    public ReviewResponseDTO create(UUID clienteId, ReviewRequestDTO dto) {
        log.info("Cliente {} tentando avaliar servico via ordem {}", clienteId, dto.serviceOrderId());

        // 1. Valida existência e perfil do autor
        UserModel cliente = userRepository.findById(clienteId)
                .orElseThrow(() -> {
                    log.warn("Falha ao avaliar: Cliente {} nao encontrado", clienteId);
                    return new RuntimeException("Cliente não encontrado");
                });

        if (!UserENUM.CLIENTE.equals(cliente.getPerfil())) {
            log.warn("Tentativa negada: Usuario {} nao tem perfil de cliente", clienteId);
            throw new RuntimeException("Apenas clientes podem avaliar serviços");
        }

        // 2. Valida a Ordem de Serviço e o vínculo com o cliente
        ServiceOrderModel ordem = serviceOrderRepository.findById(dto.serviceOrderId())
                .orElseThrow(() -> {
                    log.warn("Falha ao avaliar: Ordem {} nao encontrada", dto.serviceOrderId());
                    return new RuntimeException("Ordem não encontrada");
                });

        if (!ordem.getCliente().getId().equals(clienteId)) {
            log.error("Alerta de Seguranca: Cliente {} tentou avaliar ordem {} que nao lhe pertence", clienteId, ordem.getId());
            throw new RuntimeException("Esta ordem não pertence a você");
        }

        // 3. Regra de Negócio: Avaliação vinculada à entrega
        if (!OrderStatusEnum.COMPLETED.equals(ordem.getStatus())) {
            log.warn("Tentativa negada: Ordem {} ainda esta com status {}", ordem.getId(), ordem.getStatus());
            throw new RuntimeException("Só é possível avaliar após a ordem ser concluída");
        }

        // 4. Impede duplicidade de reviews por serviço
        boolean jaAvaliou = reviewRepository
                .findByServiceIdAndUsuarioId(ordem.getService().getId(), clienteId)
                .isPresent();

        if (jaAvaliou) {
            log.warn("Cliente {} ja avaliou o servico {}", clienteId, ordem.getService().getId());
            throw new RuntimeException("Você já avaliou este serviço");
        }

        // 5. Mapeamento e Persistência
        ReviewModel review = reviewMapper.toModel(dto);
        review.setService(ordem.getService());
        review.setUsuario(cliente);
        review.setServiceOrder(ordem);
        review.setEditado(false); // Inicialmente falso, mudará apenas se houver updateStatus futuro

        ReviewModel reviewSalva = reviewRepository.save(review);

        log.info("Avaliacao criada com sucesso — id: {}, nota: {}", reviewSalva.getId(), dto.rating());
        return reviewMapper.toResponseDTO(reviewSalva);
    }

    // Edita avaliação existente
    // Regra: apenas o próprio cliente que criou pode editar
    // Regra: não é possível editar avaliação de outro usuário
    // Regra: nota deve continuar entre 1 e 5 — validado no DTO
    @Transactional
    public ReviewResponseDTO update(UUID reviewId, UUID clienteId, ReviewRequestDTO dto) {
        ReviewModel review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada"));

        // Valida se o cliente é o dono da avaliação
        if (!review.getUsuario().getId().equals(clienteId)) {
            throw new RuntimeException("Você não tem permissão para editar esta avaliação");
        }

        review.setClassificacao(dto.rating());
        review.setComentario(dto.comentario());
        review.setEditado(true); // Marca como editada para transparência

        return reviewMapper.toResponseDTO(reviewRepository.save(review));
    }

    // Cliente deleta sua própria avaliação
    // Regra: apenas o próprio cliente que criou pode deletar
    @Transactional
    public void delete(UUID reviewId, UUID clienteId) {
        ReviewModel review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada"));

        // Valida se o cliente é o dono da avaliação
        if (!review.getUsuario().getId().equals(clienteId)) {
            throw new RuntimeException("Você não tem permissão para deletar esta avaliação");
        }

        reviewRepository.delete(review);
    }

    // ADMIN remove avaliação inadequada
    // Regra: apenas ADMIN pode usar este método
    // Regra: usado para moderação do marketplace
    @Transactional
    public void deleteByAdmin(UUID reviewId, UUID adminId) {
        UserModel admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!UserENUM.ADMIN.equals(admin.getPerfil())) {
            throw new RuntimeException("Apenas administradores podem remover avaliações");
        }

        ReviewModel review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada"));

        reviewRepository.delete(review);
    }

}
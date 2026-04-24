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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ServiceOrderRepository serviceOrderRepository;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

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

    // Cria avaliação de um serviço
    // Regra: apenas CLIENTE pode avaliar
    // Regra: a ordem deve pertencer ao cliente que está avaliando
    // Regra: a ordem deve estar COMPLETED — regra central do marketplace
    // Regra: cliente só pode avaliar uma vez por serviço — sem duplicata
    // Regra: nota deve ser entre 1 e 5 — validado no DTO com @Min e @Max
    @Transactional
    public ReviewResponseDTO create(UUID clienteId, ReviewRequestDTO dto) {
        UserModel cliente = userRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        if (!UserENUM.CLIENTE.equals(cliente.getPerfil())) {
            throw new RuntimeException("Apenas clientes podem avaliar serviços");
        }

        ServiceOrderModel ordem = serviceOrderRepository.findById(dto.serviceOrderId())
                .orElseThrow(() -> new RuntimeException("Ordem não encontrada"));

        // Valida se a ordem pertence ao cliente que está avaliando
        if (!ordem.getCliente().getId().equals(clienteId)) {
            throw new RuntimeException("Esta ordem não pertence a você");
        }

        // Regra central do marketplace: só avalia após COMPLETED
        // Garante que a avaliação é baseada em uma experiência real
        if (!OrderStatusEnum.COMPLETED.equals(ordem.getStatus())) {
            throw new RuntimeException("Só é possível avaliar após a ordem ser concluída");
        }

        // Impede avaliação duplicada — um cliente avalia um serviço apenas uma vez
        boolean jaAvaliou = reviewRepository
                .findByServiceIdAndUsuarioId(ordem.getService().getId(), clienteId)
                .isPresent();

        if (jaAvaliou) {
            throw new RuntimeException("Você já avaliou este serviço");
        }

        ReviewModel review = reviewMapper.toModel(dto);
        review.setService(ordem.getService());
        review.setUsuario(cliente);
        review.setServiceOrder(ordem);
        review.setEditado(false);
        review.setCriadoEm(LocalDateTime.now());

        return reviewMapper.toResponseDTO(reviewRepository.save(review));
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
        review.setEditadoEm(LocalDateTime.now()); // Registra quando foi editada

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

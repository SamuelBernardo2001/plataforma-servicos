package com.plataforma.servicos.repository;

import com.plataforma.servicos.entity.ReviewModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository <ReviewModel, UUID> {

    // Busca todas as avaliações de um serviço
    // Usado na listagem pública de avaliações do serviço
    // Avaliações do serviço com paginação
    Page<ReviewModel> findByServiceId(UUID serviceId, Pageable pageable);

    // Verifica se cliente já avaliou o serviço
    // Usado para impedir avaliação duplicada
    Optional<ReviewModel> findByServiceIdAndUsuarioId(UUID serviceId, UUID usuarioId);

}

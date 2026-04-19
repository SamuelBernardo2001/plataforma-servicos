package com.plataforma.servicos.repository;

import com.plataforma.servicos.entity.FavoriteModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface FavoriteRepository extends JpaRepository<FavoriteModel, UUID> {

    // Busca todos os favoritos de um usuário
    // Usado na listagem de favoritos do usuário
    // Favoritos do usuário com paginação
    Page<FavoriteModel> findByUsuarioId(UUID usuarioId, Pageable pageable);

    // Verifica se usuário já favoritou o serviço
    // Usado no toggle para saber se favorita ou desfavorita
    Optional<FavoriteModel> findByUsuarioIdAndServiceId(UUID usuarioId, UUID serviceId);
}
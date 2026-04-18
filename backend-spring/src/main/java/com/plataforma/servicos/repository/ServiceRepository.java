package com.plataforma.servicos.repository;

import com.plataforma.servicos.entity.ServiceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface ServiceRepository extends JpaRepository<ServiceModel, UUID> {

    // Listagem pública — só ativos com paginação
    // busca serviços por status ativo/inativo, categoria e prestador
    Page<ServiceModel> findByAtivo(Boolean ativo, Pageable pageable);

    // busca serviços por categoria e prestador, independente do status
    Page<ServiceModel> findByCategoriaId(UUID categoriaId, Pageable pageable);

    // Painel do prestador — todos (ativos e inativos) com paginação
    // busca serviços por prestador, independente do status
    Page<ServiceModel> findByPrestadorId(UUID prestadorId, Pageable pageable);

    // Listagem por categoria e status com paginação
    // busca serviços por categoria e status, independente do prestador
    Page<ServiceModel> findByCategoriaIdAndAtivo(UUID categoriaId, Boolean ativo, Pageable pageable);

    // Listagem pública do prestador — só ativos com paginação
    // busca serviços por prestador e status, independente da categoria
    Page<ServiceModel> findByPrestadorIdAndAtivo(UUID prestadorId, Boolean ativo, Pageable pageable);
}
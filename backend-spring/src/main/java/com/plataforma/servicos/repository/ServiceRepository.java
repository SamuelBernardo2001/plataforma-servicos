package com.plataforma.servicos.repository;

import com.plataforma.servicos.entity.ServiceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ServiceRepository extends JpaRepository<ServiceModel, UUID> {

    // busca serviços por status ativo/inativo, categoria e prestador
    List<ServiceModel> findByAtivo(Boolean ativo);

    // busca serviços por categoria e prestador, independente do status
    List<ServiceModel> findByCategoriaId(UUID categoriaId);

    // busca serviços por prestador, independente do status
    List<ServiceModel> findByPrestadorId(UUID prestadorId);

    // busca serviços por categoria e status, independente do prestador
    List<ServiceModel> findByCategoriaIdAndAtivo(UUID categoriaId, Boolean ativo);

    // busca serviços por prestador e status, independente da categoria
    List<ServiceModel> findByPrestadorIdAndAtivo(UUID prestadorId, Boolean ativo);
}
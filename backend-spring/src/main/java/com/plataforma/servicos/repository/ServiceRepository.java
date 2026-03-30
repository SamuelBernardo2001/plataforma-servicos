package com.plataforma.servicos.repository;

import com.plataforma.servicos.entity.ServiceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ServiceRepository extends JpaRepository<ServiceModel, UUID> {

    // Busca serviços por categoria — usado na listagem por categoria
    List<ServiceModel> findByCategoriaId(UUID categoriaId);

    // Busca serviços por prestador — usado no painel do prestador
    List<ServiceModel> findByPrestadorId(UUID prestadorId);

    // Busca serviços ativos por categoria — usado na listagem pública
    List<ServiceModel> findByCategoriaIdAndAtivo(UUID categoriaId, Boolean ativo);

    // Busca serviços ativos por prestador — usado no perfil do prestador
    List<ServiceModel> findByPrestadorIdAndAtivo(UUID prestadorId, Boolean ativo);
}
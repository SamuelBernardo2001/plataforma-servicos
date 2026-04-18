package com.plataforma.servicos.repository;

import com.plataforma.servicos.entity.CategoryModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<CategoryModel, UUID> {

    // Busca categorias ativas com paginação
    Page<CategoryModel> findByAtivo(Boolean ativo, Pageable pageable);

    // Verifica se já existe categoria com esse nome
    // Usado para impedir duplicidade de nome
    Optional<CategoryModel> findByNome(String nome);
}
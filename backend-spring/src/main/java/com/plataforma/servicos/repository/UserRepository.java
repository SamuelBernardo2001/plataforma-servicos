package com.plataforma.servicos.repository;

import com.plataforma.servicos.entity.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, UUID> {

    // Busca usuário pelo email para login e validação de unicidade
    Optional<UserModel> findByEmail(String email);

    // Busca usuários ativos com paginação
    // Usado no findAll() do UserService
    Page<UserModel> findByAtivo(Boolean ativo, Pageable pageable);
}

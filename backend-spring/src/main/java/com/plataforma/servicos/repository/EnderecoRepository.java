package com.plataforma.servicos.repository;

import com.plataforma.servicos.entity.EnderecoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface EnderecoRepository extends JpaRepository<EnderecoModel, UUID> {

    // Busca endereço pelo ID do usuário
    // Usado em todos os métodos do EnderecoService
    Optional<EnderecoModel> findByUserId(UUID userId);
}
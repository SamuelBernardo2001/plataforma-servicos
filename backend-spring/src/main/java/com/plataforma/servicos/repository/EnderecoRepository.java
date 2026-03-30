package com.plataforma.servicos.repository;

import com.plataforma.servicos.entity.EnderecoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface EnderecoRepository extends JpaRepository<EnderecoModel, UUID> { }
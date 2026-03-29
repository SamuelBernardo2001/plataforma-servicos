package com.plataforma.servicos.repository;

import com.plataforma.servicos.entity.ServiceImageModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ServiceImageRepository extends JpaRepository<ServiceImageModel, UUID> {
}

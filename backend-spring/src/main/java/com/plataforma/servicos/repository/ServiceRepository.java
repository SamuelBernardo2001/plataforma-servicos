package com.plataforma.servicos.repository;

import com.plataforma.servicos.entity.ServiceModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ServiceRepository extends JpaRepository<ServiceModel, UUID> {
}
package com.plataforma.servicos.repository;

import com.plataforma.servicos.entity.ReportModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReportRepository extends JpaRepository<ReportModel, UUID> {
}

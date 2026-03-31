package com.plataforma.servicos.repository;

import com.plataforma.servicos.entity.ReportModel;
import com.plataforma.servicos.entity.ReportStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReportRepository extends JpaRepository<ReportModel, UUID> {

    // Busca denúncias por status — usado pelo ADMIN para filtrar
    List<ReportModel> findByStatus(ReportStatusEnum status);
}

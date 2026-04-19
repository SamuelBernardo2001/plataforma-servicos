package com.plataforma.servicos.repository;

import com.plataforma.servicos.entity.ReportModel;
import com.plataforma.servicos.entity.ReportStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReportRepository extends JpaRepository<ReportModel, UUID> {

    // Busca denúncias por status — usado pelo ADMIN para filtrar
    // Denúncias por status com paginação — usado pelo ADMIN
    Page<ReportModel> findByStatus(ReportStatusEnum status, Pageable pageable);

    // Todas as denúncias com paginação — usado pelo ADMIN
    Page<ReportModel> findAll(Pageable pageable);
}

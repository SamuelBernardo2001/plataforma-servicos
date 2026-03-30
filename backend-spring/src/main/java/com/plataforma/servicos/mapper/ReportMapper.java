package com.plataforma.servicos.mapper;


import com.plataforma.servicos.dto.reportDTOS.ReportRequestDTO;
import com.plataforma.servicos.dto.reportDTOS.ReportResponseDTO;
import com.plataforma.servicos.entity.ReportModel;
import org.springframework.stereotype.Component;

@Component
public class ReportMapper {

    // Converte ReportModel para ReportResponseDTO
    public ReportResponseDTO toResponseDTO(ReportModel report) {
        return new ReportResponseDTO(
                report.getId(),
                report.getReporter() != null ? report.getReporter().getId() : null,
                report.getReporter() != null ? report.getReporter().getNome() : null,
                report.getReportedUser() != null ? report.getReportedUser().getId() : null,
                report.getReportedUser() != null ? report.getReportedUser().getNome() : null,
                report.getServiceOrder() != null ? report.getServiceOrder().getId() : null,
                report.getRazao(),
                report.getDescricao(),
                report.getCriadoEm()
        );
    }

    // Converte ReportRequestDTO para ReportModel
    public ReportModel toModel(ReportRequestDTO dto) {
        return ReportModel.builder()
                .razao(dto.motivo())
                .descricao(dto.descricao())
                .build();
        // reporter, reportedUser e serviceOrder são setados no Service Layer
    }
}
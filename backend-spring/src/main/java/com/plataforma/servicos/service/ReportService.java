package com.plataforma.servicos.service;

import com.plataforma.servicos.dto.reportDTOS.ReportRequestDTO;
import com.plataforma.servicos.dto.reportDTOS.ReportResponseDTO;
import com.plataforma.servicos.entity.*;
import com.plataforma.servicos.mapper.ReportMapper;
import com.plataforma.servicos.repository.ReportRepository;
import com.plataforma.servicos.repository.ServiceOrderRepository;
import com.plataforma.servicos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final ServiceOrderRepository serviceOrderRepository;
    private final ReportMapper reportMapper;

    // Lista todas as denúncias
    // Regra: apenas ADMIN pode ver todas as denúncias
    public List<ReportResponseDTO> findAll(UUID adminId) {
        UserModel admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!UserENUM.ADMIN.equals(admin.getPerfil())) {
            throw new RuntimeException("Apenas administradores podem ver as denúncias");
        }

        return reportRepository.findAll()
                .stream()
                .map(reportMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Lista denúncias por status
    // Regra: apenas ADMIN pode filtrar denúncias por status
    // Usado para o ADMIN gerenciar denúncias pendentes separadas das resolvidas
    public List<ReportResponseDTO> findByStatus(UUID adminId, ReportStatusEnum status) {
        UserModel admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!UserENUM.ADMIN.equals(admin.getPerfil())) {
            throw new RuntimeException("Apenas administradores podem ver as denúncias");
        }

        return reportRepository.findByStatus(status)
                .stream()
                .map(reportMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Cria denúncia
    // Regra: usuário não pode denunciar a si mesmo
    // Regra: usuário denunciado deve existir
    // Regra: ordem informada deve existir (quando informada)
    // Regra: toda denúncia começa com status PENDENTE
    @Transactional
    public ReportResponseDTO create(UUID reporterId, ReportRequestDTO dto) {
        UserModel reporter = userRepository.findById(reporterId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        UserModel reportedUser = userRepository.findById(dto.reportedUserId())
                .orElseThrow(() -> new RuntimeException("Usuário denunciado não encontrado"));

        // Usuário não pode denunciar a si mesmo
        if (reporterId.equals(dto.reportedUserId())) {
            throw new RuntimeException("Você não pode denunciar a si mesmo");
        }

        ReportModel report = reportMapper.toModel(dto);
        report.setReporter(reporter);
        report.setReportedUser(reportedUser);
        report.setStatus(ReportStatusEnum.PENDENTE); // toda denúncia começa pendente
        report.setCriadoEm(LocalDateTime.now());

        // Vincula à ordem se informada
        if (dto.serviceOrderId() != null) {
            ServiceOrderModel ordem = serviceOrderRepository.findById(dto.serviceOrderId())
                    .orElseThrow(() -> new RuntimeException("Ordem não encontrada"));
            report.setServiceOrder(ordem);
        }

        return reportMapper.toResponseDTO(reportRepository.save(report));
    }

    // ADMIN resolve denúncia
    // Regra: apenas ADMIN pode resolver
    // Regra: denúncia já resolvida ou rejeitada não pode ser alterada
    // Significa: denúncia era válida e ADMIN tomou providências
    @Transactional
    public ReportResponseDTO resolve(UUID reportId, UUID adminId) {
        UserModel admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!UserENUM.ADMIN.equals(admin.getPerfil())) {
            throw new RuntimeException("Apenas administradores podem resolver denúncias");
        }

        ReportModel report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Denúncia não encontrada"));

        // Denúncia já finalizada não pode ser alterada
        if (!ReportStatusEnum.PENDENTE.equals(report.getStatus())) {
            throw new RuntimeException("Apenas denúncias pendentes podem ser resolvidas");
        }

        report.setStatus(ReportStatusEnum.RESOLVIDA);

        return reportMapper.toResponseDTO(reportRepository.save(report));
    }
}

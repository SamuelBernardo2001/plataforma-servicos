package com.plataforma.servicos.service;

import com.plataforma.servicos.dto.reportDTOS.ReportResponseDTO;
import com.plataforma.servicos.entity.ReportStatusEnum;
import com.plataforma.servicos.entity.UserENUM;
import com.plataforma.servicos.entity.UserModel;
import com.plataforma.servicos.mapper.ReportMapper;
import com.plataforma.servicos.repository.ReportRepository;
import com.plataforma.servicos.repository.ServiceOrderRepository;
import com.plataforma.servicos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}

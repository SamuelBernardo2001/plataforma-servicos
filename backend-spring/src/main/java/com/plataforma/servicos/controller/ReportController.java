package com.plataforma.servicos.controller;

import com.plataforma.servicos.dto.reportDTOS.ReportResponseDTO;
import com.plataforma.servicos.entity.ReportStatusEnum;
import com.plataforma.servicos.exception.ApiResponse;
import com.plataforma.servicos.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

// @RestController → combina @Controller + @ResponseBody
// Todos os métodos retornam JSON automaticamente
@RestController

// /api/reports → prefixo de todos os endpoints desta classe
@RequestMapping("/api/reports")

// @RequiredArgsConstructor → injeta ReportService via construtor
// Padrão recomendado pelo Spring — mais seguro que @Autowired
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // CONSULTAS DO ADMIN
    // Apenas ADMIN pode ver as denúncias
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

    // GET /api/reports/admin/{adminId}
    // Lista todas as denúncias do sistema
    // Regra: apenas ADMIN pode ver todas as denúncias
    // Regra: retorna lista com todos os status
    //        (PENDENTE, RESOLVIDA, REJEITADA)
    // Usado no painel de moderação do ADMIN
    // No M7 o adminId virá do token JWT automaticamente
    @GetMapping("/admin/{adminId}")
    public ResponseEntity<ApiResponse<List<ReportResponseDTO>>> findAll(
            @PathVariable UUID adminId) {
        List<ReportResponseDTO> reports = reportService.findAll(adminId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        reports,
                        "Denúncias listadas com sucesso",
                        HttpStatus.OK.value()
                ));
    }

    // GET /api/reports/admin/{adminId}/status/{status}
    // Lista denúncias filtradas por status
    // Regra: apenas ADMIN pode filtrar denúncias
    // Usado no painel de moderação para o ADMIN
    //   gerenciar denúncias pendentes separadas das resolvidas
    // Ex: GET .../status/PENDENTE → só denúncias aguardando análise
    //     GET .../status/RESOLVIDA → só denúncias já resolvidas
    //     GET .../status/REJEITADA → só denúncias rejeitadas
    // No M7 o adminId virá do token JWT automaticamente
    @GetMapping("/admin/{adminId}/status/{status}")
    public ResponseEntity<ApiResponse<List<ReportResponseDTO>>> findByStatus(
            @PathVariable UUID adminId,
            @PathVariable ReportStatusEnum status) {
        List<ReportResponseDTO> reports = reportService.findByStatus(adminId, status);
        return ResponseEntity.ok(
                ApiResponse.success(
                        reports,
                        "Denúncias filtradas por status com sucesso",
                        HttpStatus.OK.value()
                ));
    }
}

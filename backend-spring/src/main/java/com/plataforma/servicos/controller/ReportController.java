package com.plataforma.servicos.controller;

import com.plataforma.servicos.dto.reportDTOS.ReportRequestDTO;
import com.plataforma.servicos.dto.reportDTOS.ReportResponseDTO;
import com.plataforma.servicos.entity.ReportStatusEnum;
import com.plataforma.servicos.exception.ApiResponse;
import com.plataforma.servicos.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // CRIAÇÃO DE DENÚNCIA
    // Qualquer usuário autenticado pode denunciar

    // POST /api/reports/usuario/{reporterId}
    // Cria uma denúncia contra outro usuário
    // Regra: usuário não pode denunciar a si mesmo
    // Regra: usuário denunciado deve existir
    // Regra: ordem informada deve existir (quando informada)
    // Regra: toda denúncia começa com status PENDENTE
    // No M7 o reporterId virá do token JWT automaticamente
    @PostMapping("/usuario/{reporterId}")
    public ResponseEntity<ApiResponse<ReportResponseDTO>> create(
            @PathVariable UUID reporterId,
            @Valid @RequestBody ReportRequestDTO dto) {
        ReportResponseDTO report = reportService.create(reporterId, dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        report,
                        "Denúncia registrada com sucesso",
                        HttpStatus.CREATED.value()
                ));
    }

    // MODERAÇÃO DO ADMIN
    // Apenas ADMIN pode resolver ou rejeitar

    // PATCH /api/reports/{id}/resolver/admin/{adminId}
    // ADMIN resolve a denúncia
    // Regra: apenas ADMIN pode resolver
    // Regra: apenas denúncias PENDENTES podem ser resolvidas
    // Regra: denúncia RESOLVIDA ou REJEITADA não muda mais
    // Significa: denúncia era válida e ADMIN tomou providências
    //            ex: usuário banido, conteúdo removido
    // Por que PATCH?
    //   Estamos atualizando apenas o status — não o recurso inteiro
    // No M7 o adminId virá do token JWT automaticamente
    @PatchMapping("/{id}/resolver/admin/{adminId}")
    public ResponseEntity<ApiResponse<ReportResponseDTO>> resolve(
            @PathVariable UUID id,
            @PathVariable UUID adminId) {
        ReportResponseDTO report = reportService.resolve(id, adminId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        report,
                        "Denúncia resolvida com sucesso",
                        HttpStatus.OK.value()
                ));
    }
}

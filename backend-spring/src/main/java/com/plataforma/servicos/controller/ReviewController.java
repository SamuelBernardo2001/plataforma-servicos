package com.plataforma.servicos.controller;

import com.plataforma.servicos.dto.reviewDTOS.ReviewResponseDTO;
import com.plataforma.servicos.exception.ApiResponse;
import com.plataforma.servicos.service.ReviewService;
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

// /api/reviews → prefixo de todos os endpoints desta classe
@RequestMapping("/api/reviews")

// @RequiredArgsConstructor → injeta ReviewService via construtor
// Padrão recomendado pelo Spring — mais seguro que @Autowired
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // CONSULTAS PÚBLICAS
    // Qualquer pessoa pode ver as avaliações
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

    // GET /api/reviews/service/{serviceId}
    // Lista todas as avaliações de um serviço
    // Regra: avaliações são públicas — qualquer pessoa pode ver
    // Regra: serviço deve existir
    // Usado no frontend para exibir avaliações na página do serviço
    // Importante para credibilidade do marketplace
    @GetMapping("/service/{serviceId}")
    public ResponseEntity<ApiResponse<List<ReviewResponseDTO>>> findByService(
            @PathVariable UUID serviceId) {
        List<ReviewResponseDTO> reviews = reviewService.findByService(serviceId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        reviews,
                        "Avaliações listadas com sucesso",
                        HttpStatus.OK.value()
                ));
    }
}

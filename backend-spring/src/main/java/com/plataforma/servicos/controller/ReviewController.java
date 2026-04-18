package com.plataforma.servicos.controller;

import com.plataforma.servicos.dto.reviewDTOS.ReviewRequestDTO;
import com.plataforma.servicos.dto.reviewDTOS.ReviewResponseDTO;
import com.plataforma.servicos.exception.ApiResponse;
import com.plataforma.servicos.service.ReviewService;
import com.plataforma.servicos.util.PaginatedResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // Lista todas as avaliações de um serviço
    // Regra: avaliações são públicas — qualquer pessoa pode ver
    // Regra: serviço deve existir
    // Usado no frontend para exibir avaliações na página do serviço
    // Importante para credibilidade do marketplace
    // GET /api/reviews/service/{serviceId}?page=0&size=20
    @GetMapping("/service/{serviceId}")
    public ResponseEntity<ApiResponse<PaginatedResponse<ReviewResponseDTO>>> findByService(
            @PathVariable UUID serviceId,
            @PageableDefault(size = 20, sort = "criadoEm",
                    direction = Sort.Direction.DESC) Pageable pageable) {
        PaginatedResponse<ReviewResponseDTO> reviews =
                reviewService.findByService(serviceId, pageable);
        return ResponseEntity.ok(
                ApiResponse.success(
                        reviews,
                        "Avaliações listadas com sucesso",
                        HttpStatus.OK.value()
                ));
    }

    // OPERAÇÕES DO CLIENTE

    // POST /api/reviews/cliente/{clienteId}
    // Cria avaliação de um serviço
    // Regra: apenas CLIENTE pode avaliar
    // Regra: a ordem informada deve pertencer ao cliente
    // Regra: a ordem deve estar COMPLETED
    //        → regra central do marketplace
    //        → garante que avaliação vem de experiência real
    // Regra: cliente só pode avaliar um serviço uma vez
    //        → sem avaliação duplicada
    // Regra: nota entre 1 e 5 — validado no DTO com @Min e @Max
    // No M7 o clienteId virá do token JWT automaticamente
    @PostMapping("/cliente/{clienteId}")
    public ResponseEntity<ApiResponse<ReviewResponseDTO>> create(
            @PathVariable UUID clienteId,
            @Valid @RequestBody ReviewRequestDTO dto) {
        ReviewResponseDTO review = reviewService.create(clienteId, dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        review,
                        "Avaliação criada com sucesso",
                        HttpStatus.CREATED.value()
                ));
    }

    // PUT /api/reviews/{id}/cliente/{clienteId}
    // Edita avaliação existente
    // Regra: apenas o próprio cliente que criou pode editar
    // Regra: nota deve continuar entre 1 e 5
    // Regra: marca editado = true e registra editadoEm
    //        → transparência para prestador e outros clientes
    //        → igual ao WhatsApp que mostra "editada"
    // No M7 o clienteId virá do token JWT automaticamente
    @PutMapping("/{id}/cliente/{clienteId}")
    public ResponseEntity<ApiResponse<ReviewResponseDTO>> update(
            @PathVariable UUID id,
            @PathVariable UUID clienteId,
            @Valid @RequestBody ReviewRequestDTO dto) {
        ReviewResponseDTO review = reviewService.update(id, clienteId, dto);
        return ResponseEntity.ok(
                ApiResponse.success(
                        review,
                        "Avaliação atualizada com sucesso",
                        HttpStatus.OK.value()
                ));
    }

    // DELETE /api/reviews/{id}/cliente/{clienteId}
    // Cliente deleta sua própria avaliação
    // Regra: apenas o próprio cliente que criou pode deletar
    // Regra: após deletar o cliente pode avaliar novamente
    //        pois a restrição de duplicata é baseada na existência
    // No M7 o clienteId virá do token JWT automaticamente
    @DeleteMapping("/{id}/cliente/{clienteId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id,
            @PathVariable UUID clienteId) {
        reviewService.delete(id, clienteId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        null,
                        "Avaliação removida com sucesso",
                        HttpStatus.OK.value()
                ));
    }

    // OPERAÇÕES DO ADMIN
    // Moderação do marketplace

    // DELETE /api/reviews/{id}/admin/{adminId}
    // ADMIN remove avaliação inadequada
    // Regra: apenas ADMIN pode usar este endpoint
    // Regra: usado para moderação — remover avaliações
    //        falsas, ofensivas ou inadequadas
    // Importante para manter qualidade do marketplace
    // No M7 o adminId virá do token JWT automaticamente
    @DeleteMapping("/{id}/admin/{adminId}")
    public ResponseEntity<ApiResponse<Void>> deleteByAdmin(
            @PathVariable UUID id,
            @PathVariable UUID adminId) {
        reviewService.deleteByAdmin(id, adminId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        null,
                        "Avaliação removida pelo administrador",
                        HttpStatus.OK.value()
                ));
    }
}

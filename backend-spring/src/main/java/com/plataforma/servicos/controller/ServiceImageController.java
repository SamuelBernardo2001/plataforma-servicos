package com.plataforma.servicos.controller;

import com.plataforma.servicos.dto.serviceImagesDTO.ServiceImageRequestDTO;
import com.plataforma.servicos.dto.serviceImagesDTO.ServiceImageResponseDTO;
import com.plataforma.servicos.exception.ApiResponse;
import com.plataforma.servicos.service.ServiceImageService;
import com.plataforma.servicos.util.PaginatedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

// /api/v1/service-images → prefixo de todos os endpoints desta classe
@RequestMapping("/api/service-images")

// @RequiredArgsConstructor → injeta ServiceImageService via construtor
// Padrão recomendado pelo Spring — mais seguro que @Autowired
@RequiredArgsConstructor
@Tag(name = "Imagens do Serviço", description = "Endpoints para gerenciamento da galeria de fotos dos serviços")
public class ServiceImageController {

    private final ServiceImageService serviceImageService;

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // CONSULTAS PÚBLICAS
    // Qualquer pessoa pode ver as imagens
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

    // GET /api/service-images/service/{serviceId}
    // Lista todas as imagens de um serviço
    // Regra: imagens são públicas — qualquer pessoa pode ver
    // Regra: serviço deve existir
    // Usado no frontend para exibir galeria de fotos do serviço
    // Cliente visualiza antes de contratar
    // GET /api/v1/service-images/service/{serviceId}?page=0&size=5
    @Operation(summary = "Listar imagens de um serviço", description = "Retorna uma lista paginada de imagens associadas a um serviço específico. Acesso público.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Imagens listadas com sucesso")
    @GetMapping("/service/{serviceId}")
    public ResponseEntity<ApiResponse<PaginatedResponse<ServiceImageResponseDTO>>> findByService(
            @PathVariable UUID serviceId,
            @PageableDefault(size = 5, sort = "id",
                    direction = Sort.Direction.ASC) Pageable pageable) {
        PaginatedResponse<ServiceImageResponseDTO> images =
                serviceImageService.findByService(serviceId, pageable);
        return ResponseEntity.ok(
                ApiResponse.success(
                        images,
                        "Imagens do serviço listadas com sucesso",
                        HttpStatus.OK.value()
                ));
    }

    // OPERAÇÕES DO PRESTADOR
    // Apenas o dono do serviço pode adicionar e remover

    // POST /api/service-images/service/{serviceId}/prestador/{prestadorId}
    // Adiciona imagem ao serviço
    // Regra: apenas o próprio PRESTADOR dono do serviço pode adicionar
    // Regra: serviço deve estar ativo
    // Regra: limite máximo de 10 imagens por serviço
    // No M7 o prestadorId virá do token JWT automaticamente
    // No M7 será integrado com Cloudinary para upload real
    @Operation(summary = "Adicionar imagem ao serviço", description = "Permite ao prestador adicionar fotos à galeria do seu serviço. Limite máximo de 10 imagens.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Imagem adicionada com sucesso"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Limite de imagens excedido ou serviço inativo"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Usuário não tem permissão para alterar este serviço")
    })
    @PostMapping("/service/{serviceId}/prestador/{prestadorId}")
    public ResponseEntity<ApiResponse<ServiceImageResponseDTO>> addImage(
            @PathVariable UUID serviceId,
            @PathVariable UUID prestadorId,
            @Valid @RequestBody ServiceImageRequestDTO dto) {
        ServiceImageResponseDTO image = serviceImageService.addImage(
                serviceId, prestadorId, dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        image,
                        "Imagem adicionada com sucesso",
                        HttpStatus.CREATED.value()
                ));
    }

    // DELETE /api/service-images/{id}/prestador/{prestadorId}
    // Remove imagem do serviço
    // Regra: apenas o próprio PRESTADOR dono do serviço pode remover
    // Regra: imagem deve existir
    // No M7 o prestadorId virá do token JWT automaticamente
    // No M7 será integrado com Cloudinary para deletar do storage
    @Operation(summary = "Remover imagem", description = "Remove uma imagem específica da galeria. Apenas o prestador dono do serviço pode realizar esta operação.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Imagem removida com sucesso"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Imagem não encontrada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Usuário sem permissão")
    })
    @DeleteMapping("/{id}/prestador/{prestadorId}")
    public ResponseEntity<ApiResponse<Void>> removeImage(
            @PathVariable UUID id,
            @PathVariable UUID prestadorId) {
        serviceImageService.removeImage(id, prestadorId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        null,
                        "Imagem removida com sucesso",
                        HttpStatus.OK.value()
                ));
    }
}
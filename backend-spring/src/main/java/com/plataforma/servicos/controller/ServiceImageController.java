package com.plataforma.servicos.controller;

import com.plataforma.servicos.dto.serviceImagesDTO.ServiceImageRequestDTO;
import com.plataforma.servicos.dto.serviceImagesDTO.ServiceImageResponseDTO;
import com.plataforma.servicos.exception.ApiResponse;
import com.plataforma.servicos.service.ServiceImageService;
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

// /api/v1/service-images → prefixo de todos os endpoints desta classe
@RequestMapping("/api/service-images")

// @RequiredArgsConstructor → injeta ServiceImageService via construtor
// Padrão recomendado pelo Spring — mais seguro que @Autowired
@RequiredArgsConstructor
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
    @GetMapping("/service/{serviceId}")
    public ResponseEntity<ApiResponse<List<ServiceImageResponseDTO>>> findByService(
            @PathVariable UUID serviceId) {
        List<ServiceImageResponseDTO> images = serviceImageService.findByService(serviceId);
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
}

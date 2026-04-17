package com.plataforma.servicos.controller;

import com.plataforma.servicos.dto.serviceImagesDTO.ServiceImageResponseDTO;
import com.plataforma.servicos.exception.ApiResponse;
import com.plataforma.servicos.service.ServiceImageService;
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
}

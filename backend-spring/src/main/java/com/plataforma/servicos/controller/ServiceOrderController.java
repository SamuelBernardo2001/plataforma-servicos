package com.plataforma.servicos.controller;

import com.plataforma.servicos.dto.serviceOrderDTOS.ServiceOrderResponseDTO;
import com.plataforma.servicos.exception.ApiResponse;
import com.plataforma.servicos.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

// @RestController → combina @Controller + @ResponseBody
// Todos os métodos retornam JSON automaticamente
@RestController

// /api/service-orders → prefixo de todos os endpoints desta classe
@RequestMapping("/api/service-orders")

// @RequiredArgsConstructor → injeta OrderService via construtor
// Padrão recomendado pelo Spring — mais seguro que @Autowired
@RequiredArgsConstructor
public class ServiceOrderController {

    private final OrderService orderService;

    // CONSULTAS

    // GET /api/service-orders/{id}/usuario/{usuarioId}
    // Busca uma ordem específica pelo UUID
    // Regra: apenas cliente ou prestador da ordem podem visualizar
    // Regra: retorna erro se UUID for inválido
    // Regra: retorna erro se ordem não existir
    // No M7 o usuarioId virá do token JWT automaticamente
    @GetMapping("/{id}/usuario/{usuarioId}")
    public ResponseEntity<ApiResponse<ServiceOrderResponseDTO>> findById(
            @PathVariable UUID id,
            @PathVariable UUID usuarioId) {
        ServiceOrderResponseDTO order = orderService.findById(id, usuarioId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        order,
                        "Ordem encontrada",
                        HttpStatus.OK.value()
                ));
    }
}

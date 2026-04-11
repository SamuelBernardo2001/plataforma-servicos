package com.plataforma.servicos.controller;

import com.plataforma.servicos.dto.serviceOrderDTOS.ServiceOrderRequestDTO;
import com.plataforma.servicos.dto.serviceOrderDTOS.ServiceOrderResponseDTO;
import com.plataforma.servicos.entity.OrderStatusEnum;
import com.plataforma.servicos.exception.ApiResponse;
import com.plataforma.servicos.service.OrderService;
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

    // GET /api/service-orders/cliente/{clienteId}
    // Lista todas as ordens de um cliente
    // Regra: cliente só vê suas próprias ordens
    // Regra: retorna lista vazia se não tiver ordens
    // Usado no painel do cliente para acompanhar contratações
    // No M7 o clienteId virá do token JWT automaticamente
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<ApiResponse<List<ServiceOrderResponseDTO>>> findByCliente(
            @PathVariable UUID clienteId) {
        List<ServiceOrderResponseDTO> orders = orderService.findByCliente(clienteId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        orders,
                        "Ordens do cliente listadas com sucesso",
                        HttpStatus.OK.value()
                ));
    }

    // GET /api/service-orders/prestador/{prestadorId}
    // Lista todas as ordens recebidas pelo prestador
    // Regra: prestador só vê suas próprias ordens recebidas
    // Regra: retorna lista vazia se não tiver ordens
    // Usado no painel do prestador para gerenciar pedidos recebidos
    // No M7 o prestadorId virá do token JWT automaticamente
    @GetMapping("/prestador/{prestadorId}")
    public ResponseEntity<ApiResponse<List<ServiceOrderResponseDTO>>> findByPrestador(
            @PathVariable UUID prestadorId) {
        List<ServiceOrderResponseDTO> orders = orderService.findByPrestador(prestadorId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        orders,
                        "Ordens do prestador listadas com sucesso",
                        HttpStatus.OK.value()
                ));
    }

    // GET /api/service-orders/cliente/{clienteId}/status/{status}
    // Lista ordens do cliente filtradas por status
    // Regra: cliente só vê suas próprias ordens
    // Usado no painel do cliente para filtrar por situação
    // Ex: ver apenas ordens REQUESTED, ACCEPTED, COMPLETED ou CANCELED
    // No M7 o clienteId virá do token JWT automaticamente
    @GetMapping("/cliente/{clienteId}/status/{status}")
    public ResponseEntity<ApiResponse<List<ServiceOrderResponseDTO>>> findByStatus(
            @PathVariable UUID clienteId,
            @PathVariable OrderStatusEnum status) {
        List<ServiceOrderResponseDTO> orders = orderService.findByStatus(clienteId, status);
        return ResponseEntity.ok(
                ApiResponse.success(
                        orders,
                        "Ordens filtradas por status com sucesso",
                        HttpStatus.OK.value()
                ));
    }

    // CRIAÇÃO DE ORDEM
    // Apenas CLIENTE pode criar

    // POST /api/service-orders/cliente/{clienteId}
    // Cria nova ordem de serviço (contratação)
    // Regra: apenas CLIENTE pode criar ordem
    // Regra: serviço deve estar ativo
    // Regra: cliente não pode contratar seu próprio serviço
    // Regra: cliente não pode ter ordem REQUESTED ou ACCEPTED
    //        para o mesmo serviço — sem duplicata ativa
    // No M7 o clienteId virá do token JWT automaticamente
    @PostMapping("/cliente/{clienteId}")
    public ResponseEntity<ApiResponse<ServiceOrderResponseDTO>> create(
            @PathVariable UUID clienteId,
            @Valid @RequestBody ServiceOrderRequestDTO dto) {
        ServiceOrderResponseDTO order = orderService.create(clienteId, dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        order,
                        "Ordem de serviço criada com sucesso",
                        HttpStatus.CREATED.value()
                ));
    }

}

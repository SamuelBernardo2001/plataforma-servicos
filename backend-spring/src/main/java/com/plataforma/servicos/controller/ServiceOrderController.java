package com.plataforma.servicos.controller;

import com.plataforma.servicos.dto.serviceOrderDTOS.ServiceOrderRequestDTO;
import com.plataforma.servicos.dto.serviceOrderDTOS.ServiceOrderResponseDTO;
import com.plataforma.servicos.entity.OrderStatusEnum;
import com.plataforma.servicos.exception.ApiResponse;
import com.plataforma.servicos.service.OrderService;
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

    // Lista todas as ordens de um cliente
    // Regra: cliente só vê suas próprias ordens
    // Regra: retorna lista vazia se não tiver ordens
    // Usado no painel do cliente para acompanhar contratações
    // No M7 o clienteId virá do token JWT automaticamente
    // GET /api/service-orders/cliente/{clienteId}?page=0&size=20
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<ApiResponse<PaginatedResponse<ServiceOrderResponseDTO>>> findByCliente(
            @PathVariable UUID clienteId,
            @PageableDefault(size = 20, sort = "criadoEm",
                    direction = Sort.Direction.DESC) Pageable pageable) {
        PaginatedResponse<ServiceOrderResponseDTO> orders =
                orderService.findByCliente(clienteId, pageable);
        return ResponseEntity.ok(
                ApiResponse.success(
                        orders,
                        "Ordens do cliente listadas com sucesso",
                        HttpStatus.OK.value()
                ));
    }

    // Lista todas as ordens recebidas pelo prestador
    // Regra: prestador só vê suas próprias ordens recebidas
    // Regra: retorna lista vazia se não tiver ordens
    // Usado no painel do prestador para gerenciar pedidos recebidos
    // No M7 o prestadorId virá do token JWT automaticamente
    // GET /api/service-orders/prestador/{prestadorId}?page=0&size=20
    @GetMapping("/prestador/{prestadorId}")
    public ResponseEntity<ApiResponse<PaginatedResponse<ServiceOrderResponseDTO>>> findByPrestador(
            @PathVariable UUID prestadorId,
            @PageableDefault(size = 20, sort = "criadoEm",
                    direction = Sort.Direction.DESC) Pageable pageable) {
        PaginatedResponse<ServiceOrderResponseDTO> orders =
                orderService.findByPrestador(prestadorId, pageable);
        return ResponseEntity.ok(
                ApiResponse.success(
                        orders,
                        "Ordens do prestador listadas com sucesso",
                        HttpStatus.OK.value()
                ));
    }

    // Lista ordens do cliente filtradas por status
    // Regra: cliente só vê suas próprias ordens
    // Usado no painel do cliente para filtrar por situação
    // Ex: ver apenas ordens REQUESTED, ACCEPTED, COMPLETED ou CANCELED
    // No M7 o clienteId virá do token JWT automaticamente
    // GET /api/service-orders/cliente/{clienteId}/status/{status}?page=0&size=20
    @GetMapping("/cliente/{clienteId}/status/{status}")
    public ResponseEntity<ApiResponse<PaginatedResponse<ServiceOrderResponseDTO>>> findByStatus(
            @PathVariable UUID clienteId,
            @PathVariable OrderStatusEnum status,
            @PageableDefault(size = 20, sort = "criadoEm",
                    direction = Sort.Direction.DESC) Pageable pageable) {
        PaginatedResponse<ServiceOrderResponseDTO> orders =
                orderService.findByStatus(clienteId, status, pageable);
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

    // FLUXO DE STATUS
    // Controla o ciclo de vida da ordem

    // PATCH /api/service-orders/{id}/status/usuario/{usuarioId}
    // Atualiza o status da ordem
    // Fluxo completo:
    //   REQUESTED → ACCEPTED   (prestador aceita)
    //   REQUESTED → CANCELED   (prestador recusa ou cliente cancela)
    //   ACCEPTED  → COMPLETED  (prestador conclui)
    //   ACCEPTED  → CANCELED   (prestador cancela)
    // Regra: ordem COMPLETED ou CANCELED não muda mais de status
    // Regra: apenas participantes da ordem podem alterar status
    // Regra: concluidoEm é setado automaticamente ao COMPLETED
    //        liberando avaliação do cliente
    // Por que PATCH e não PUT?
    //   PUT → atualiza o recurso inteiro
    //   PATCH → atualiza apenas um campo (status)
    // No M7 o usuarioId virá do token JWT automaticamente
    @PatchMapping("/{id}/status/usuario/{usuarioId}")
    public ResponseEntity<ApiResponse<ServiceOrderResponseDTO>> updateStatus(
            @PathVariable UUID id,
            @PathVariable UUID usuarioId,
            @RequestParam OrderStatusEnum novoStatus) {
        ServiceOrderResponseDTO order = orderService.updateStatus(id, usuarioId, novoStatus);
        return ResponseEntity.ok(
                ApiResponse.success(
                        order,
                        "Status da ordem atualizado com sucesso",
                        HttpStatus.OK.value()
                ));
    }

    // VERIFICAÇÕES

    // GET /api/service-orders/verificar-concluida
    // Verifica se existe ordem COMPLETED entre cliente e serviço
    // Usado pelo frontend antes de mostrar botão de avaliação
    // Regra: retorna true se existe ordem COMPLETED
    //        retorna false se não existe
    // Isso garante que avaliação só aparece após contratação real
    @GetMapping("/verificar-concluida")
    public ResponseEntity<ApiResponse<Boolean>> verificarOrdemConcluida(
            @RequestParam UUID clienteId,
            @RequestParam UUID serviceId) {
        boolean existe = orderService.existeOrdemConcluida(clienteId, serviceId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        existe,
                        existe
                                ? "Existe ordem concluída — avaliação liberada"
                                : "Não existe ordem concluída — avaliação bloqueada",
                        HttpStatus.OK.value()
                ));
    }

}

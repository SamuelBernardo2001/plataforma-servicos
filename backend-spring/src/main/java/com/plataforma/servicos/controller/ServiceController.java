package com.plataforma.servicos.controller;

import com.plataforma.servicos.dto.ServiceDTOS.ServiceRequestDTO;
import com.plataforma.servicos.dto.ServiceDTOS.ServiceResponseDTO;
import com.plataforma.servicos.exception.ApiResponse;
import com.plataforma.servicos.service.ServicoService;
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

// /api/services → prefixo de todos os endpoints desta classe
@RequestMapping("/api/services")

// @RequiredArgsConstructor → injeta ServiceService via construtor
// Padrão recomendado pelo Spring — mais seguro que @Autowired
@RequiredArgsConstructor
public class ServiceController {

    private final ServicoService serviceService;

    // CONSULTAS PÚBLICAS
    // Qualquer pessoa pode ver — sem autenticação

    // GET /api/services
    // Lista todos os serviços ATIVOS do marketplace
    // Regra: apenas serviços com ativo = true aparecem
    // Regra: filtro feito direto no banco via findByAtivo(true)
    // Quem usa: clientes buscando serviços, visitantes
    // No M7 não precisará de autenticação — endpoint público
    @GetMapping
    public ResponseEntity<ApiResponse<List<ServiceResponseDTO>>> findAll() {
        List<ServiceResponseDTO> services = serviceService.findAll();
        return ResponseEntity.ok(
                ApiResponse.success(
                        services,
                        "Serviços listados com sucesso",
                        HttpStatus.OK.value()
                ));
    }

    // GET /api/services/{id}
    // Busca um serviço específico pelo UUID
    // Regra: serviço desativado não é encontrado
    // Regra: retorna erro se UUID for inválido
    // Quem usa: cliente visualizando detalhes do serviço
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ServiceResponseDTO>> findById(
            @PathVariable UUID id) {
        ServiceResponseDTO service = serviceService.findById(id);
        return ResponseEntity.ok(
                ApiResponse.success(
                        service,
                        "Serviço encontrado",
                        HttpStatus.OK.value()
                ));
    }

    // GET /api/services/category/{categoriaId}
    // Lista serviços ATIVOS de uma categoria específica
    // Regra: categoria deve existir
    // Regra: apenas serviços ativos da categoria aparecem
    // Regra: filtro feito direto no banco via findByCategoriaIdAndAtivo()
    // Quem usa: cliente filtrando serviços por categoria
    @GetMapping("/category/{categoriaId}")
    public ResponseEntity<ApiResponse<List<ServiceResponseDTO>>> findByCategory(
            @PathVariable UUID categoriaId) {
        List<ServiceResponseDTO> services = serviceService.findByCategory(categoriaId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        services,
                        "Serviços da categoria listados com sucesso",
                        HttpStatus.OK.value()
                ));
    }

    // GET /api/services/prestador/{prestadorId}
    // Lista serviços ATIVOS de um prestador específico
    // Usado na página pública do prestador vista pelo cliente
    // Regra: apenas serviços ativos aparecem para o público
    // Regra: filtro via findByPrestadorIdAndAtivo()
    // Quem usa: cliente visitando perfil público do prestador
    @GetMapping("/prestador/{prestadorId}")
    public ResponseEntity<ApiResponse<List<ServiceResponseDTO>>> findByPrestador(
            @PathVariable UUID prestadorId) {
        List<ServiceResponseDTO> services = serviceService.findByPrestador(prestadorId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        services,
                        "Serviços do prestador listados com sucesso",
                        HttpStatus.OK.value()
                ));
    }

    // PAINEL DO PRESTADOR
    // Apenas o próprio prestador acessa

    // GET /api/services/meus/{prestadorId}
    // Lista TODOS os serviços do prestador (ativos e inativos)
    // Usado no painel do prestador para gerenciar seus serviços
    // Diferente do findByPrestador() que só retorna ativos para o público
    // Regra: prestador vê todos os seus serviços incluindo desativados
    // No M7 o prestadorId virá do token JWT automaticamente
    @GetMapping("/meus/{prestadorId}")
    public ResponseEntity<ApiResponse<List<ServiceResponseDTO>>> findAllByPrestador(
            @PathVariable UUID prestadorId) {
        List<ServiceResponseDTO> services = serviceService.findAllByPrestador(prestadorId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        services,
                        "Seus serviços listados com sucesso",
                        HttpStatus.OK.value()
                ));
    }

    // OPERAÇÕES DO PRESTADOR
    // Apenas PRESTADOR pode criar, atualizar e desativar

    // POST /api/services/prestador/{prestadorId}
    // Cria novo serviço no marketplace
    // Regra: apenas usuários com perfil PRESTADOR podem criar
    // Regra: prestador deve estar ativo no sistema
    // Regra: categoria informada deve existir e estar ativa
    // @Valid → ativa validações do ServiceRequestDTO:
    //   @NotBlank em nome, descricao, telefoneContato
    //   @NotNull e @Positive em preco
    //   @NotNull em categoriaId
    // No M7 o prestadorId virá do token JWT automaticamente
    @PostMapping("/prestador/{prestadorId}")
    public ResponseEntity<ApiResponse<ServiceResponseDTO>> create(
            @PathVariable UUID prestadorId,
            @Valid @RequestBody ServiceRequestDTO dto) {
        ServiceResponseDTO service = serviceService.create(prestadorId, dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        service,
                        "Serviço cadastrado com sucesso",
                        HttpStatus.CREATED.value()
                ));
    }

}

package com.plataforma.servicos.controller;

import com.plataforma.servicos.dto.ServiceDTOS.ServiceRequestDTO;
import com.plataforma.servicos.dto.ServiceDTOS.ServiceResponseDTO;
import com.plataforma.servicos.exception.ApiResponse;
import com.plataforma.servicos.service.ServicoService;
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

// /api/services → prefixo de todos os endpoints desta classe
@RequestMapping("/api/services")

// @RequiredArgsConstructor → injeta ServiceService via construtor
// Padrão recomendado pelo Spring — mais seguro que @Autowired
@RequiredArgsConstructor
public class ServiceController {

    private final ServicoService serviceService;

    // CONSULTAS PÚBLICAS
    // Qualquer pessoa pode ver — sem autenticação

    // Lista todos os serviços ATIVOS do marketplace
    // Regra: apenas serviços com ativo = true aparecem
    // Regra: filtro feito direto no banco via findByAtivo(true)
    // Quem usa: clientes buscando serviços, visitantes
    // No M7 não precisará de autenticação — endpoint público
    // GET /api/services?page=0&size=20&sort=criadoEm,desc
    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponse<ServiceResponseDTO>>> findAll(
            @PageableDefault(size = 20, sort = "criadoEm",
                    direction = Sort.Direction.DESC) Pageable pageable) {
        PaginatedResponse<ServiceResponseDTO> services = serviceService.findAll(pageable);
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

    // Lista serviços ATIVOS de uma categoria específica
    // Regra: categoria deve existir
    // Regra: apenas serviços ativos da categoria aparecem
    // Regra: filtro feito direto no banco via findByCategoriaIdAndAtivo()
    // Quem usa: cliente filtrando serviços por categoria
    // GET /api/services/category/{categoriaId}?page=0&size=20
    @GetMapping("/category/{categoriaId}")
    public ResponseEntity<ApiResponse<PaginatedResponse<ServiceResponseDTO>>> findByCategory(
            @PathVariable UUID categoriaId,
            @PageableDefault(size = 20, sort = "criadoEm",
                    direction = Sort.Direction.DESC) Pageable pageable) {
        PaginatedResponse<ServiceResponseDTO> services =
                serviceService.findByCategory(categoriaId, pageable);
        return ResponseEntity.ok(
                ApiResponse.success(
                        services,
                        "Serviços da categoria listados com sucesso",
                        HttpStatus.OK.value()
                ));
    }

    // Lista serviços ATIVOS de um prestador específico
    // Usado na página pública do prestador vista pelo cliente
    // Regra: apenas serviços ativos aparecem para o público
    // Regra: filtro via findByPrestadorIdAndAtivo()
    // Quem usa: cliente visitando perfil público do prestador
    // GET /api/services/prestador/{prestadorId}?page=0&size=20
    @GetMapping("/prestador/{prestadorId}")
    public ResponseEntity<ApiResponse<PaginatedResponse<ServiceResponseDTO>>> findByPrestador(
            @PathVariable UUID prestadorId,
            @PageableDefault(size = 20, sort = "criadoEm",
                    direction = Sort.Direction.DESC) Pageable pageable) {
        PaginatedResponse<ServiceResponseDTO> services =
                serviceService.findByPrestador(prestadorId, pageable);
        return ResponseEntity.ok(
                ApiResponse.success(
                        services,
                        "Serviços do prestador listados com sucesso",
                        HttpStatus.OK.value()
                ));
    }

    // PAINEL DO PRESTADOR
    // Apenas o próprio prestador acessa

    // Lista TODOS os serviços do prestador (ativos e inativos)
    // Usado no painel do prestador para gerenciar seus serviços
    // Diferente do findByPrestador() que só retorna ativos para o público
    // Regra: prestador vê todos os seus serviços incluindo desativados
    // No M7 o prestadorId virá do token JWT automaticamente
    // GET /api/services/meus/{prestadorId}?page=0&size=20
    @GetMapping("/meus/{prestadorId}")
    public ResponseEntity<ApiResponse<PaginatedResponse<ServiceResponseDTO>>> findAllByPrestador(
            @PathVariable UUID prestadorId,
            @PageableDefault(size = 20, sort = "criadoEm",
                    direction = Sort.Direction.DESC) Pageable pageable) {
        PaginatedResponse<ServiceResponseDTO> services =
                serviceService.findAllByPrestador(prestadorId, pageable);
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

    // PUT /api/services/{id}/prestador/{prestadorId}
    // Atualiza dados do serviço
    // Regra: apenas o próprio prestador pode atualizar seu serviço
    // Regra: serviço desativado não pode ser atualizado
    // Regra: nova categoria deve existir e estar ativa
    // @Valid → valida ServiceRequestDTO
    // No M7 o prestadorId virá do token JWT automaticamente
    @PutMapping("/{id}/prestador/{prestadorId}")
    public ResponseEntity<ApiResponse<ServiceResponseDTO>> update(
            @PathVariable UUID id,
            @PathVariable UUID prestadorId,
            @Valid @RequestBody ServiceRequestDTO dto) {
        ServiceResponseDTO service = serviceService.update(id, prestadorId, dto);
        return ResponseEntity.ok(
                ApiResponse.success(
                        service,
                        "Serviço atualizado com sucesso",
                        HttpStatus.OK.value()
                ));
    }

    // DELETE /api/services/{id}/prestador/{prestadorId}
    // Desativa serviço — soft delete
    // Regra: apenas o próprio prestador pode desativar seu serviço
    // Regra: serviço NÃO é deletado do banco — apenas ativo = false
    // Regra: serviço desativado não aparece nas listagens públicas
    // Regra: prestador ainda vê o serviço desativado no painel
    // No M7 o prestadorId virá do token JWT automaticamente
    @DeleteMapping("/{id}/prestador/{prestadorId}")
    public ResponseEntity<ApiResponse<Void>> deactivate(
            @PathVariable UUID id,
            @PathVariable UUID prestadorId) {
        serviceService.deactivate(id, prestadorId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        null,
                        "Serviço desativado com sucesso",
                        HttpStatus.OK.value()
                ));
    }

    // AVALIAÇÕES

    // GET /api/services/{id}/media-avaliacao
    // Retorna a média de avaliações de um serviço
    // Regra: retorna 0.0 se não houver avaliações
    // Quem usa: frontend para exibir estrelas na listagem
    @GetMapping("/{id}/media-avaliacao")
    public ResponseEntity<ApiResponse<Double>> getMediaAvaliacao(
            @PathVariable UUID id) {
        Double media = serviceService.calcularMediaAvaliacao(id);
        return ResponseEntity.ok(
                ApiResponse.success(
                        media,
                        "Média de avaliações calculada",
                        HttpStatus.OK.value()
                ));
    }

}

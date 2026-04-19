package com.plataforma.servicos.controller;

import com.plataforma.servicos.dto.CategoryDTOS.CategoryRequestDTO;
import com.plataforma.servicos.dto.CategoryDTOS.CategoryResponseDTO;
import com.plataforma.servicos.exception.ApiResponse;
import com.plataforma.servicos.service.CategoryService;
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

// /api/categories → prefixo de todos os endpoints desta classe
@RequestMapping("/api/categories")

// @RequiredArgsConstructor → injeta CategoryService via construtor
// Padrão recomendado pelo Spring — mais seguro que @Autowired
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // CONSULTAS PÚBLICAS
    // Qualquer pessoa pode ver — sem autenticação

    // Lista todas as categorias ATIVAS do sistema
    // Regra: apenas categorias com ativo = true aparecem
    // Regra: filtro feito direto no banco via findByAtivo(true)
    // Quem usa: cliente ao buscar serviços, prestador ao cadastrar serviço
    // No M7 não precisará de autenticação — endpoint público
    // GET /api/categories?page=0&size=20&sort=nome,asc
    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponse<CategoryResponseDTO>>> findAll(
            @PageableDefault(size = 20, sort = "nome",
                    direction = Sort.Direction.ASC) Pageable pageable) {
        PaginatedResponse<CategoryResponseDTO> categories = categoryService.findAll(pageable);
        return ResponseEntity.ok(
                ApiResponse.success(
                        categories,
                        "Categorias listadas com sucesso",
                        HttpStatus.OK.value()
                ));
    }

    // GET /api/categories/{id}
    // Busca uma categoria específica pelo UUID
    // Regra: retorna erro se UUID for inválido
    // Regra: retorna erro se categoria não existir
    // Quem usa: frontend para exibir detalhes da categoria
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponseDTO>> findById(
            @PathVariable UUID id) {
        CategoryResponseDTO category = categoryService.findById(id);
        return ResponseEntity.ok(
                ApiResponse.success(
                        category,
                        "Categoria encontrada",
                        HttpStatus.OK.value()
                ));
    }

    // OPERAÇÕES DO ADMIN
    // Apenas ADMIN pode criar, editar e desativar

    // POST /api/categories/admin/{adminId}
    // Cria nova categoria no sistema
    // Regra: apenas ADMIN pode criar categorias
    // Regra: nome da categoria deve ser único no sistema
    // @Valid  → ativa validações do CategoryRequestDTO:
    //   @NotBlank em nome e descricao
    // No M7 o adminId virá do token JWT automaticamente
    @PostMapping("/admin/{adminId}")
    public ResponseEntity<ApiResponse<CategoryResponseDTO>> create(
            @PathVariable UUID adminId,
            @Valid @RequestBody CategoryRequestDTO dto) {
        CategoryResponseDTO category = categoryService.create(adminId, dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        category,
                        "Categoria criada com sucesso",
                        HttpStatus.CREATED.value()
                ));
    }

    // PUT /api/categories/{id}/admin/{adminId}
    // Atualiza nome e descrição de uma categoria
    // Regra: apenas ADMIN pode editar categorias
    // Regra: novo nome deve ser único se for diferente do atual
    // Regra: serviços vinculados continuam funcionando normalmente
    // No M7 o adminId virá do token JWT automaticamente
    @PutMapping("/{id}/admin/{adminId}")
    public ResponseEntity<ApiResponse<CategoryResponseDTO>> update(
            @PathVariable UUID id,
            @PathVariable UUID adminId,
            @Valid @RequestBody CategoryRequestDTO dto) {
        CategoryResponseDTO category = categoryService.update(id, adminId, dto);
        return ResponseEntity.ok(
                ApiResponse.success(
                        category,
                        "Categoria atualizada com sucesso",
                        HttpStatus.OK.value()
                ));
    }

    // DELETE /api/categories/{id}/admin/{adminId}
    // Desativa categoria — soft delete
    // Regra: apenas ADMIN pode desativar categorias
    // Regra: categoria NÃO é deletada do banco — apenas ativo = false
    // Regra: serviços que usavam essa categoria ficam pendentes — prestador precisará cadastrar uma nova categoria
    // Regra: categoria já desativada não pode ser desativada novamente
    // No M7 o adminId virá do token JWT automaticamente
    @DeleteMapping("/{id}/admin/{adminId}")
    public ResponseEntity<ApiResponse<Void>> deactivate(
            @PathVariable UUID id,
            @PathVariable UUID adminId) {
        categoryService.deactivate(id, adminId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        null,
                        "Categoria desativada com sucesso — serviços vinculados ficam pendentes",
                        HttpStatus.OK.value()
                ));
    }
}

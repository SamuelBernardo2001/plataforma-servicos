package com.plataforma.servicos.controller;

import com.plataforma.servicos.dto.CategoryDTOS.CategoryResponseDTO;
import com.plataforma.servicos.exception.ApiResponse;
import com.plataforma.servicos.service.CategoryService;
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

// /api/categories → prefixo de todos os endpoints desta classe
@RequestMapping("/api/categories")

// @RequiredArgsConstructor → injeta CategoryService via construtor
// Padrão recomendado pelo Spring — mais seguro que @Autowired
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // CONSULTAS PÚBLICAS
    // Qualquer pessoa pode ver — sem autenticação

    // GET /api/categories
    // Lista todas as categorias ATIVAS do sistema
    // Regra: apenas categorias com ativo = true aparecem
    // Regra: filtro feito direto no banco via findByAtivo(true)
    // Quem usa: cliente ao buscar serviços, prestador ao cadastrar serviço
    // No M7 não precisará de autenticação — endpoint público
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponseDTO>>> findAll() {
        List<CategoryResponseDTO> categories = categoryService.findAll();
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
}

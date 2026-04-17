package com.plataforma.servicos.controller;

import com.plataforma.servicos.dto.favoriteDTOS.FavoriteResponseDTO;
import com.plataforma.servicos.exception.ApiResponse;
import com.plataforma.servicos.service.FavoriteService;
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

// /api/favorites → prefixo de todos os endpoints desta classe
@RequestMapping("/api/v1/favorites")

// @RequiredArgsConstructor → injeta FavoriteService via construtor
// Padrão recomendado pelo Spring — mais seguro que @Autowired
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // CONSULTAS
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

    // GET /api/favorites/usuario/{usuarioId}
    // Lista todos os favoritos do usuário
    // Regra: usuário só vê seus próprios favoritos
    // Regra: usuário deve existir
    // Usado no frontend para exibir lista de favoritos do usuário
    // No M7 o usuarioId virá do token JWT automaticamente
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<ApiResponse<List<FavoriteResponseDTO>>> findByUsuario(
            @PathVariable UUID usuarioId) {
        List<FavoriteResponseDTO> favorites = favoriteService.findByUsuario(usuarioId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        favorites,
                        "Favoritos listados com sucesso",
                        HttpStatus.OK.value()
                ));
    }
}

package com.plataforma.servicos.controller;

import com.plataforma.servicos.dto.favoriteDTOS.FavoriteResponseDTO;
import com.plataforma.servicos.exception.ApiResponse;
import com.plataforma.servicos.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // GET /api/favorites/usuario/{usuarioId}/service/{serviceId}
    // Verifica se um serviço é favorito do usuário
    // Regra: retorna true se favoritado, false se não
    // Usado no frontend para mostrar coração cheio ou vazio
    // no botão de favoritar da listagem de serviços
    // No M7 o usuarioId virá do token JWT automaticamente
    @GetMapping("/usuario/{usuarioId}/service/{serviceId}")
    public ResponseEntity<ApiResponse<Boolean>> isFavorito(
            @PathVariable UUID usuarioId,
            @PathVariable UUID serviceId) {
        boolean favorito = favoriteService.isFavorito(usuarioId, serviceId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        favorito,
                        favorito
                                ? "Serviço está nos favoritos"
                                : "Serviço não está nos favoritos",
                        HttpStatus.OK.value()
                ));
    }

    // TOGGLE

    // POST /api/favorites/toggle/usuario/{usuarioId}/service/{serviceId}
    // Favorita ou desfavorita um serviço (toggle)
    // Regra: se já favoritou → desfavorita
    //        se não favoritou → favorita
    // Regra: serviço deve estar ativo
    // Regra: usuário não pode favoritar seu próprio serviço
    // Regra: unicidade — um usuário só favorita um serviço uma vez
    // Por que POST e não PUT?
    //   POST → cria recurso (favorito)
    //   O toggle cria ou deleta — POST é mais semântico aqui
    // Por que retorna String em vez de objeto?
    //   A mensagem muda dependendo da ação
    //   "Serviço adicionado aos favoritos" ou
    //   "Serviço removido dos favoritos"
    //   Frontend usa essa mensagem para feedback ao usuário
    // No M7 o usuarioId virá do token JWT automaticamente
    @PostMapping("/toggle/usuario/{usuarioId}/service/{serviceId}")
    public ResponseEntity<ApiResponse<String>> toggle(
            @PathVariable UUID usuarioId,
            @PathVariable UUID serviceId) {
        String mensagem = favoriteService.toggle(usuarioId, serviceId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        mensagem,
                        mensagem,
                        HttpStatus.OK.value()
                ));
    }
}

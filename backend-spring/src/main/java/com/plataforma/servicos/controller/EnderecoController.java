package com.plataforma.servicos.controller;

import com.plataforma.servicos.dto.EnderecoDTOS.EnderecoResponseDTO;
import com.plataforma.servicos.exception.ApiResponse;
import com.plataforma.servicos.service.EnderecoService;
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

// /api/enderecos → prefixo de todos os endpoints desta classe
@RequestMapping("/api/enderecos")

// @RequiredArgsConstructor → injeta EnderecoService via construtor
// Padrão recomendado pelo Spring — mais seguro que @Autowired
@RequiredArgsConstructor
public class EnderecoController {

    private final EnderecoService enderecoService;

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // CONSULTAS
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

    // GET /api/enderecos/usuario/{usuarioId}
    // Busca o endereço do próprio usuário
    // Regra: usuário só pode ver seu próprio endereço
    // Regra: retorna erro se endereço não cadastrado ainda
    // Usado no frontend para exibir e editar endereço do perfil
    // No M7 o usuarioId virá do token JWT automaticamente
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<ApiResponse<EnderecoResponseDTO>> findByUsuario(
            @PathVariable UUID usuarioId) {
        EnderecoResponseDTO endereco = enderecoService.findByUsuario(usuarioId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        endereco,
                        "Endereço encontrado",
                        HttpStatus.OK.value()
                ));
    }

    // GET /api/enderecos/cliente/{clienteId}/prestador/{prestadorId}
    // Prestador visualiza endereço do cliente
    // Regra: prestador só consegue ver o endereço se existir
    //        ordem ACCEPTED entre ele e o cliente
    // Regra: endereço some após ordem COMPLETED — privacidade
    // Regra: protege privacidade do cliente — prestador não
    //        consegue ver endereço sem contratação ativa
    // Usado quando prestador precisa saber onde executar o serviço
    // No M7 o prestadorId virá do token JWT automaticamente
    @GetMapping("/cliente/{clienteId}/prestador/{prestadorId}")
    public ResponseEntity<ApiResponse<EnderecoResponseDTO>> findByUsuarioParaPrestador(
            @PathVariable UUID clienteId,
            @PathVariable UUID prestadorId) {
        EnderecoResponseDTO endereco = enderecoService
                .findByUsuarioParaPrestador(clienteId, prestadorId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        endereco,
                        "Endereço do cliente encontrado",
                        HttpStatus.OK.value()
                ));
    }
}

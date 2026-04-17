package com.plataforma.servicos.controller;

import com.plataforma.servicos.dto.EnderecoDTOS.EnderecoRequestDTO;
import com.plataforma.servicos.dto.EnderecoDTOS.EnderecoResponseDTO;
import com.plataforma.servicos.exception.ApiResponse;
import com.plataforma.servicos.service.EnderecoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // CADASTRO

    // POST /api/enderecos/usuario/{usuarioId}
    // Cadastra endereço após criação da conta
    // Regra: usuário deve estar ativo
    // Regra: usuário só pode ter um endereço — unicidade
    // Regra: endereço não é obrigatório no cadastro —
    //        pode ser cadastrado depois
    // No M7 o usuarioId virá do token JWT automaticamente
    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<ApiResponse<EnderecoResponseDTO>> create(
            @PathVariable UUID usuarioId,
            @Valid @RequestBody EnderecoRequestDTO dto) {
        EnderecoResponseDTO endereco = enderecoService.create(usuarioId, dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        endereco,
                        "Endereço cadastrado com sucesso",
                        HttpStatus.CREATED.value()
                ));
    }

    // ATUALIZAÇÕES

    // PUT /api/enderecos/usuario/{usuarioId}
    // Atualiza endereço completo do usuário
    // Regra: usuário só pode atualizar seu próprio endereço
    // Regra: todos os campos são atualizados de uma vez
    // Usado quando usuário quer trocar de endereço completamente
    // No M7 o usuarioId virá do token JWT automaticamente
    @PutMapping("/usuario/{usuarioId}")
    public ResponseEntity<ApiResponse<EnderecoResponseDTO>> update(
            @PathVariable UUID usuarioId,
            @Valid @RequestBody EnderecoRequestDTO dto) {
        EnderecoResponseDTO endereco = enderecoService.update(usuarioId, dto);
        return ResponseEntity.ok(
                ApiResponse.success(
                        endereco,
                        "Endereço atualizado com sucesso",
                        HttpStatus.OK.value()
                ));
    }
}

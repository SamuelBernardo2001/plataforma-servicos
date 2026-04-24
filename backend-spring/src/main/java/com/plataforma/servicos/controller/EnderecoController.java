package com.plataforma.servicos.controller;

import com.plataforma.servicos.dto.EnderecoDTOS.EnderecoPatchDTO;
import com.plataforma.servicos.dto.EnderecoDTOS.EnderecoRequestDTO;
import com.plataforma.servicos.dto.EnderecoDTOS.EnderecoResponseDTO;
import com.plataforma.servicos.exception.ApiResponse;
import com.plataforma.servicos.service.EnderecoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Endereços", description = "Endpoints para gestão de endereços de usuários e privacidade de localização")
public class EnderecoController {

    private final EnderecoService enderecoService;

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // CONSULTAS
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

    // GET /api/enderecos/usuario/{usuarioId}
    // Busca o endereço do próprio usuário
    @Operation(summary = "Buscar endereço do usuário", description = "Retorna o endereço vinculado ao perfil do usuário solicitante.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Endereço encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Endereço não cadastrado")
    })
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
    @Operation(summary = "Visualizar endereço do cliente (Uso do Prestador)",
            description = "Permite que um prestador veja o endereço do cliente apenas se houver uma ordem de serviço ACEITA (ACCEPTED) entre ambos. Garante a privacidade fora do período de prestação.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Endereço do cliente liberado para visualização")
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
    @Operation(summary = "Cadastrar endereço", description = "Realiza o cadastro único de endereço para um usuário ativo.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Endereço cadastrado com sucesso"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Usuário já possui endereço ou dados inválidos")
    })
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
    @Operation(summary = "Atualizar endereço completo (PUT)", description = "Substitui todos os dados do endereço atual pelos novos dados informados.")
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

    // PATCH /api/enderecos/usuario/{usuarioId}
    @Operation(summary = "Atualizar campos parciais (PATCH)", description = "Permite atualizar apenas campos específicos do endereço (ex: apenas o complemento) sem necessidade de enviar o objeto todo.")
    @PatchMapping("/usuario/{usuarioId}")
    public ResponseEntity<ApiResponse<EnderecoResponseDTO>> patch(
            @PathVariable UUID usuarioId,
            @Valid @RequestBody EnderecoPatchDTO dto) {
        EnderecoResponseDTO endereco = enderecoService.patch(usuarioId, dto);
        return ResponseEntity.ok(
                ApiResponse.success(
                        endereco,
                        "Endereço atualizado com sucesso",
                        HttpStatus.OK.value()
                ));
    }

    // REMOÇÃO

    // DELETE /api/enderecos/usuario/{usuarioId}
    @Operation(summary = "Remover endereço", description = "Exclui o endereço do perfil do usuário. No M7, haverá validação para impedir a exclusão caso existam ordens ativas em andamento.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Endereço removido com sucesso")
    @DeleteMapping("/usuario/{usuarioId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID usuarioId) {
        enderecoService.delete(usuarioId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        null,
                        "Endereço removido com sucesso",
                        HttpStatus.OK.value()
                ));
    }
}
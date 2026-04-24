package com.plataforma.servicos.controller;

import com.plataforma.servicos.dto.UserDTOS.*;
import com.plataforma.servicos.exception.ApiResponse;
import com.plataforma.servicos.service.UserService;
import com.plataforma.servicos.util.PaginatedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
// Indica que essa classe é um Controller REST
// Todos os métodos retornam JSON automaticamente
@RestController

// @RequestMapping → define o prefixo de todos os endpoints desta classe
// /api/users → versionamento da API conforme documentação seção 8
@RequestMapping("/api/users")

// @RequiredArgsConstructor → injeta UserService via construtor
// Padrão recomendado pelo Spring — mais seguro que @Autowired
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários e autenticação")
public class UserController {

    private final UserService userService;

    // Lista todos os usuários ativos do sistema
    // Regra: apenas usuários com ativo = true aparecem
    // Quem usa: ADMIN para gerenciar usuários
    // GET /api/users?page=0&size=20&sort=criadoEm,desc
    @Operation(summary = "Listar usuários", description = "Retorna uma lista paginada de usuários ativos. Apenas usuários com status ativo=true são exibidos.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Usuários listados com sucesso")
    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponse<UserResponseDTO>>> findAll(
            @PageableDefault(size = 20, sort = "criadoEm",
                    direction = Sort.Direction.DESC) Pageable pageable) {
        PaginatedResponse<UserResponseDTO> users = userService.findAll(pageable);
        return ResponseEntity.ok(
                ApiResponse.success(
                        users,
                        "Usuários listados com sucesso",
                        HttpStatus.OK.value()
                ));
    }

    // GET /api/users/{id}
    // Busca um usuário específico pelo ID
    // Regra: usuário desativado não é encontrado
    // Quem usa: qualquer usuário autenticado (M7 controlará acesso por role)
    @Operation(summary = "Buscar por ID", description = "Retorna os detalhes de um usuário específico através do seu UUID.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuário não encontrado ou desativado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> findById(@PathVariable UUID id) {
        UserResponseDTO user = userService.findById(id);
        return ResponseEntity.ok(
                ApiResponse.success(user, "Usuário encontrado", HttpStatus.OK.value())
        );
    }

    // POST /api/auth/register
    // Cadastra novo usuário no sistema
    // @Valid → ativa as validações do UserRequestDTO
    //   (@NotBlank, @Email, @Size, @NotNull)
    // Se alguma validação falhar → GlobalExceptionHandler
    //   captura e retorna VALIDATION_ERROR automaticamente
    // Regra: email único no sistema
    // Status 201 → Created (recurso criado com sucesso)
    @Operation(summary = "Cadastrar usuário", description = "Cria um novo usuário no sistema com validação de e-mail único.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados")
    })
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponseDTO>> create(
            @Valid @RequestBody UserRequestDTO dto) {
        UserResponseDTO user = userService.create(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        user,
                        "Usuário cadastrado com sucesso",
                        HttpStatus.CREATED.value()
                ));
    }

    // POST /api/users/login
    // Autentica usuário com email e senha
    // Por enquanto retorna os dados do usuário
    // No M7 será substituído por JWT + tokens
    @Operation(summary = "Realizar login", description = "Autentica o usuário no sistema através de e-mail e senha.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponseDTO>> login(
            @Valid @RequestBody UserLoginDTO dto) {
        UserResponseDTO user = userService.login(dto);
        return ResponseEntity.ok(
                ApiResponse.success(user, "Login realizado com sucesso", HttpStatus.OK.value())
        );
    }

    // PUT /api/users/{id}/profile
    // Atualiza nome e telefone do usuário
    // Regra: apenas nome e telefone podem ser alterados
    // Regra: email e perfil não mudam após cadastro
    // @Valid → valida UserUpdateDTO (@NotBlank nos campos)
    @Operation(summary = "Atualizar perfil", description = "Permite a alteração apenas do nome e telefone do usuário.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Perfil atualizado com sucesso")
    @PutMapping("/{id}/profile")
    public ResponseEntity<ApiResponse<UserResponseDTO>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UserUpdateDTO dto) {
        UserResponseDTO user = userService.update(id, dto);
        return ResponseEntity.ok(
                ApiResponse.success(user, "Perfil atualizado com sucesso", HttpStatus.OK.value())
        );
    }

    // PUT /api/users/{id}/password
    // Altera senha do usuário
    // Regra: exige senha atual + nova senha + confirmação
    // Regra: nova senha diferente da atual
    // Regra: confirmação igual à nova senha
    // No M7 a senha será criptografada com BCrypt
    @Operation(summary = "Alterar senha", description = "Atualiza a senha do usuário exigindo a senha atual e confirmação da nova.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Senha alterada com sucesso"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Dados da senha inválidos ou divergentes")
    })
    @PutMapping("/{id}/password")
    public ResponseEntity<ApiResponse<Void>> updatePassword(
            @PathVariable UUID id,
            @Valid @RequestBody UserPasswordDTO dto) {
        userService.updatePassword(id, dto);
        return ResponseEntity.ok(
                ApiResponse.success(null, "Senha alterada com sucesso", HttpStatus.OK.value())
        );
    }

    // DELETE /api/users/{id}
    // Desativa usuário (soft delete)
    // Regra: usuário não é deletado do banco — apenas ativo = false
    // Regra: mantém histórico de ordens e avaliações
    // Status 200 com null em data → operação sem retorno de dados
    @Operation(summary = "Desativar usuário", description = "Realiza a desativação lógica (soft delete) do usuário no sistema.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Usuário desativado com sucesso")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deactivate(@PathVariable UUID id) {
        userService.deactivate(id);
        return ResponseEntity.ok(
                ApiResponse.success(null, "Usuário desativado com sucesso", HttpStatus.OK.value())
        );
    }
}
package com.plataforma.servicos.controller;

import com.plataforma.servicos.dto.UserDTOS.UserLoginDTO;
import com.plataforma.servicos.dto.UserDTOS.UserRequestDTO;
import com.plataforma.servicos.dto.UserDTOS.UserResponseDTO;
import com.plataforma.servicos.exception.ApiResponse;
import com.plataforma.servicos.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class UserController {

    private final UserService userService;

    // GET /api/users
    // Lista todos os usuários ativos do sistema
    // Regra: apenas usuários com ativo = true aparecem
    // Quem usa: ADMIN para gerenciar usuários
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> findAll() {
        List<UserResponseDTO> users = userService.findAll();
        return ResponseEntity.ok(
                ApiResponse.success(users, "Usuários listados com sucesso", HttpStatus.OK.value())
        );
    }

    // GET /api/users/{id}
    // Busca um usuário específico pelo ID
    // Regra: usuário desativado não é encontrado
    // Quem usa: qualquer usuário autenticado (M7 controlará acesso por role)
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

    // POST /api/v1/users/login
    // Autentica usuário com email e senha
    // Por enquanto retorna os dados do usuário
    // No M7 será substituído por JWT + tokens
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponseDTO>> login(
            @Valid @RequestBody UserLoginDTO dto) {
        UserResponseDTO user = userService.findByEmail(dto.email());
        return ResponseEntity.ok(
                ApiResponse.success(user, "Login realizado com sucesso", HttpStatus.OK.value())
        );
    }
}

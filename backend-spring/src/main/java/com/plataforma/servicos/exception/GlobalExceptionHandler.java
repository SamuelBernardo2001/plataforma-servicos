package com.plataforma.servicos.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

// @RestControllerAdvice → intercepta exceções de todos os Controllers
// Centraliza o tratamento de erros em um único lugar
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Trata RuntimeException — erros de regras de negócio
    // Ex: "Email já cadastrado", "Serviço não encontrado",
    //     "Apenas PRESTADOR pode cadastrar serviços"
    // Todos os Services lançam RuntimeException para regras de negócio
    // Status 400 → Bad Request (requisição inválida)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRuntimeException(
            RuntimeException ex,
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                        ex.getMessage(),
                        "BUSINESS_ERROR",
                        HttpStatus.BAD_REQUEST.value()
                ));
    }

    // Trata MethodArgumentNotValidException — erros de validação dos DTOs
    // Acontece quando @NotBlank, @Email, @Size, @Min, @Max falham
    // Ex: email inválido, senha muito curta, campo obrigatório vazio
    // Status 400 → Bad Request
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        // Coleta todas as mensagens de erro de validação dos campos
        // Ex: "Nome é obrigatório", "Email inválido", "Senha deve ter no mínimo 8 caracteres"
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                        message,
                        "VALIDATION_ERROR",
                        HttpStatus.BAD_REQUEST.value()
                ));
    }

    // Trata IllegalArgumentException — argumentos inválidos
    // Ex: UUID em formato inválido na URL
    // Status 400 → Bad Request
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                        ex.getMessage(),
                        "INVALID_ARGUMENT",
                        HttpStatus.BAD_REQUEST.value()
                ));
    }
}

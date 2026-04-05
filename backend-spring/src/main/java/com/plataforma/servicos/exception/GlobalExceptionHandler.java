package com.plataforma.servicos.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

}

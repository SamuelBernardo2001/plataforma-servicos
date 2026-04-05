package com.plataforma.servicos.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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

    // Trata NullPointerException — erros inesperados de null
    // Não deveria acontecer em produção mas é bom ter tratamento
    // Status 500 → Internal Server Error
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse<Void>> handleNullPointerException(
            NullPointerException ex,
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(
                        "Erro interno do servidor",
                        "INTERNAL_ERROR",
                        HttpStatus.INTERNAL_SERVER_ERROR.value()
                ));
    }

    // Trata Exception — qualquer outro erro não tratado acima
    // Última linha de defesa — captura tudo que passou pelos outros handlers
    // Status 500 → Internal Server Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(
                        "Erro interno do servidor",
                        "INTERNAL_ERROR",
                        HttpStatus.INTERNAL_SERVER_ERROR.value()
                ));
    }

    // Trata erros de conversão de tipo na URL
// Ex: passar {id} literal em vez de um UUID real
// Ex: passar "abc" em vez de um UUID válido
// Status 400 → Bad Request
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatchException(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {

        String message = String.format(
                "Valor inválido para o parâmetro '%s' — esperado: %s",
                ex.getName(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "tipo desconhecido"
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                        message,
                        "INVALID_PARAMETER",
                        HttpStatus.BAD_REQUEST.value()
                ));
    }
}

package com.plataforma.servicos.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// @JsonInclude(NON_NULL) → campos nulos não aparecem no JSON
// Ex: em sucesso o campo "error" não aparece
// Ex: em erro o campo "data" não aparece
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse <T> {

    // Dados retornados em caso de sucesso
    // Null em caso de erro — não aparece no JSON por causa do @JsonInclude
    private T data;

    // Código HTTP da resposta (200, 201, 400, 404, 500, etc.)
    private Integer status;

    // Mensagem legível para o frontend exibir ao usuário
    // Ex: "Usuário criado com sucesso", "Email já cadastrado"
    private String message;

    // Tipo do erro — só aparece em respostas de erro
    // Ex: "VALIDATION_ERROR", "NOT_FOUND", "UNAUTHORIZED"
    // Null em caso de sucesso — não aparece no JSON
    private String error;

    // Momento exato em que a resposta foi gerada
    // Útil para logs e debug
    private LocalDateTime timestamp;

    // Método estático para criar resposta de ERRO
    // Uso: ApiResponse.error("Email já cadastrado", "VALIDATION_ERROR", 400)
    public static <T> ApiResponse<T> error(String message, String error, Integer status) {
        return ApiResponse.<T>builder()
                .status(status)
                .message(message)
                .error(error)
                .timestamp(LocalDateTime.now())
                .build();
    }

}

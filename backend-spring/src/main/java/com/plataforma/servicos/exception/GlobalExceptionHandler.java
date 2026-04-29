package com.plataforma.servicos.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.stream.Collectors;

// @RestControllerAdvice → intercepta exceções de todos os Controllers
// Centraliza o tratamento de erros em um único lugar
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Captura e padroniza exceções de Regras de Negócio (RuntimeException).
     * * O processo segue este fluxo:
     * 1. Interceptação: O Spring monitora todos os Controllers. Se um erro do tipo
     * RuntimeException for lançado (via 'throw new RuntimeException'), este método assume o controle.
     * 2. Log de Erro: Registra o erro no console/arquivo de log, informando a URI que causou o problema
     * e a mensagem, facilitando o rastreio técnico sem expor dados sensíveis ao usuário.
     * 3. Padronização da Resposta: Cria um objeto 'ApiResponse' uniforme. Isso garante que o
     * desenvolvedor frontend sempre receba a mesma estrutura, independente do erro.
     * 4. Status HTTP: Define o status como 400 (Bad Request), indicando que a requisição não
     * pôde ser processada devido a uma violação de regra de negócio.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRuntimeException(
            RuntimeException ex,
            HttpServletRequest request) {

        // Log detalhado para o desenvolvedor identificar onde e por que ocorreu o erro
        log.error("Erro de regra de negocio na rota [{}]: {}", request.getRequestURI(), ex.getMessage());

        // Retorno padronizado para o cliente (Frontend/Mobile)
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                        ex.getMessage(),       // Mensagem que você escreveu no Service
                        "BUSINESS_ERROR",      // Código interno para o Frontend identificar o tipo de erro
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

    /**
     * Handler Global para Exceções Não Tratadas (A última linha de defesa).
     * * O processo segue este fluxo:
     * 1. Captura Universal: Intercepta qualquer erro que não foi capturado pelos handlers
     * específicos, evitando que a aplicação "quebre" e retorne uma página HTML padrão do Tomcat.
     * 2. Log de Diagnóstico Crítico: Diferente dos erros de negócio, aqui o log registra
     * o StackTrace completo (ex). Isso é vital para que o desenvolvedor descubra a causa
     * exata de um erro inesperado em produção.
     * 3. Máscara de Segurança: Oculta detalhes técnicos do usuário final. Em vez de enviar
     * nomes de classes Java ou erros de SQL, retorna apenas uma mensagem genérica e segura.
     * 4. Status HTTP 500: Define o status como 'Internal Server Error', sinalizando que
     * o problema está no servidor e não na requisição do cliente.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        // Loga o método HTTP, a URL e o erro com o Stack Trace completo para depuração
        log.error("ERRO CRÍTICO NÃO TRATADO em {} {}: {}",
                request.getMethod(),
                request.getRequestURI(),
                ex.getMessage(),
                ex);

        // Retorna uma resposta opaca para o cliente, preservando a segurança do sistema
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(
                        "Ocorreu um erro interno inesperado. Por favor, tente novamente mais tarde.",
                        "INTERNAL_SERVER_ERROR",
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

    // Trata conflitos de atualizacao simultanea (Optimistic Locking)
    // Acontece quando dois usuarios tentam atualizar o mesmo registro
    // ao mesmo tempo — o segundo recebe este erro
    // Ex: prestador aceita ordem enquanto cliente tenta cancelar
    // Status 409 → Conflict (conflito com estado atual do recurso)
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ApiResponse<Void>> handleOptimisticLockingException(
            ObjectOptimisticLockingFailureException ex,
            HttpServletRequest request) {

        log.warn("Conflito de atualizacao simultanea em {} {}: {}",
                request.getMethod(),
                request.getRequestURI(),
                ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(
                        "Este registro foi atualizado por outro usuario. " +
                                "Atualize a pagina e tente novamente.",
                        "OPTIMISTIC_LOCKING_ERROR",
                        HttpStatus.CONFLICT.value()
                ));
    }
}

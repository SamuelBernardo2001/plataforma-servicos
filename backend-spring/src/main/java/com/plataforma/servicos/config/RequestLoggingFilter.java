package com.plataforma.servicos.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

// @Component → registra como bean do Spring
// @Order(1) → executa antes de qualquer outro filtro
//   garante que o requestId está disponível para todos os logs
@Component
@Order(1)
public class RequestLoggingFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    // Chave do MDC para o ID de correlação
    // Usado no logback-spring.xml para incluir em todos os logs da requisição
    private static final String REQUEST_ID_KEY = "requestId";
    private static final String METHOD_KEY = "method";
    private static final String PATH_KEY = "path";

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Gera requestId único para correlacionar todos os logs desta requisição
        // Permite rastrear uma requisição do início ao fim nos logs
        // Ex: [requestId=a1b2c3d4] GET /api/services -> 200 (45ms)
        String requestId = UUID.randomUUID().toString().substring(0, 8);

        // Registra no cabeçalho de resposta para o cliente poder rastrear
        httpResponse.setHeader("X-Request-Id", requestId);

        // Registra no MDC — disponível para todos os logs nesta thread
        // O Logback inclui automaticamente em todos os log.info(), log.error(), etc.
        MDC.put(REQUEST_ID_KEY, requestId);
        MDC.put(METHOD_KEY, httpRequest.getMethod());
        MDC.put(PATH_KEY, httpRequest.getRequestURI());

        // Marca o tempo de início para calcular duração da requisição
        long startTime = System.currentTimeMillis();

        // Loga o início da requisição
        log.info(">> {} {} [requestId={}]",
                httpRequest.getMethod(),
                httpRequest.getRequestURI(),
                requestId);

        try {
            // Continua o processamento da requisição
            // Passa pelo Controller → Service → Repository
            chain.doFilter(request, response);
        } finally {
            // Calcula a duração total da requisição
            long duration = System.currentTimeMillis() - startTime;

            // Loga o fim da requisição com status HTTP e tempo de resposta
            // Ex: << GET /api/services -> 200 (45ms) [requestId=a1b2c3d4]
            log.info("<< {} {} -> {} ({}ms) [requestId={}]",
                    httpRequest.getMethod(),
                    httpRequest.getRequestURI(),
                    httpResponse.getStatus(),
                    duration,
                    requestId);

            // IMPORTANTE: limpa o MDC após a requisição
            // Sem isso o contexto vaza para outras requisições na mesma thread
            // Em servidores com thread pool (Tomcat) isso causa bugs difíceis de rastrear
            MDC.clear();
        }
    }
}
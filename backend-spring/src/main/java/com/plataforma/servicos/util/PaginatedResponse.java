package com.plataforma.servicos.util;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

// Padroniza o retorno de listagens paginadas
// Em vez de retornar o Page<T> do Spring diretamente
// (que tem muitos campos desnecessários) retornamos
// apenas o que o frontend precisa
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedResponse<T> {

    // Lista de itens da página atual
    private List<T> content;

    // Número da página atual (começa em 0)
    private int page;

    // Quantidade de itens por página
    private int size;

    // Total de itens em todas as páginas
    private long totalElements;

    // Total de páginas disponíveis
    private int totalPages;

    // Se é a primeira página
    private boolean first;

    // Se é a última página
    private boolean last;

    // Método estático para criar PaginatedResponse a partir de Page<T>
    // Uso: PaginatedResponse.of(page)
    public static <T> PaginatedResponse<T> of(Page<T> page) {
        return PaginatedResponse.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }
}
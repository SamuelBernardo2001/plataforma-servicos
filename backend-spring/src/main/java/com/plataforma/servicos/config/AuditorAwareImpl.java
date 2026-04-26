package com.plataforma.servicos.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

// @Component → registra essa classe como bean do Spring
// O Spring injeta automaticamente no JPA Auditing
@Component("auditorAwareImpl")
public class AuditorAwareImpl implements AuditorAware<String> {

    // getCurrentAuditor → retorna quem está fazendo a operação atual
    // O Spring chama esse método automaticamente antes de cada INSERT e UPDATE
    // para preencher os campos @CreatedBy e @LastModifiedBy
    @Override
    public Optional<String> getCurrentAuditor() {

        // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
        // IMPLEMENTAÇÃO ATUAL — M5 (sem JWT)
        // Retorna "SYSTEM" para todas as operações
        // pois ainda não temos autenticação implementada
        // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
        return Optional.of("SYSTEM");

        // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
        // IMPLEMENTAÇÃO FUTURA — M6 (com JWT)
        // Quando o Spring Security + JWT for implementado
        // substituir pelo código abaixo:
        //
        // Authentication authentication = SecurityContextHolder
        //         .getContext()
        //         .getAuthentication();
        //
        // if (authentication == null
        //         || !authentication.isAuthenticated()
        //         || authentication.getPrincipal().equals("anonymousUser")) {
        //     return Optional.of("SYSTEM");
        // }
        //
        // UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        // return Optional.of(userDetails.getUsername()); // email do usuario
        // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    }
}
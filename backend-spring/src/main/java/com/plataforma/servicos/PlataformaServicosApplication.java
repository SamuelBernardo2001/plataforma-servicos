package com.plataforma.servicos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication

// @EnableJpaAuditing → ativa a auditoria automatica do Spring Data JPA
// auditorAwareRef → referencia o bean AuditorAwareImpl pelo nome
// que retorna quem esta fazendo a operacao (@CreatedBy e @LastModifiedBy)
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
public class PlataformaServicosApplication {
    public static void main(String[] args) {
        SpringApplication.run(PlataformaServicosApplication.class, args);
    }
}
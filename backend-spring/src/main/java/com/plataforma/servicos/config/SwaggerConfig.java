package com.plataforma.servicos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

// @Configuration → indica que essa classe contém configurações do Spring
// O Bean OpenAPI personaliza as informações exibidas no Swagger UI
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        // Título exibido no topo do Swagger UI
                        .title("Plataforma de Serviços — API REST")
                        // Descrição do sistema
                        .description("""
                                Marketplace de serviços locais que conecta prestadores
                                (construção, barbearia, elétrica, etc.) a clientes
                                com contratação, avaliações e histórico real.
                                
                                **Perfis de usuário:**
                                - CLIENTE → busca e contrata serviços
                                - PRESTADOR → cadastra e gerencia serviços
                                - ADMIN → modera a plataforma
                                
                                **Fluxo de contratação:**
                                REQUESTED → ACCEPTED → COMPLETED → Avaliação liberada
                                """)
                        // Versão atual da API
                        .version("v1.0")
                        // Informações de contato
                        .contact(new Contact()
                                .name("Samuel Bernardo")
                                .email("samuel@email.com")
                                .url("https://github.com/SamuelBernardo2001/plataforma-servicos"))
                        // Licença
                        .license(new License()
                                .name("MIT License")))
                // Servidor local para testes
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de desenvolvimento")
                ));
    }
}
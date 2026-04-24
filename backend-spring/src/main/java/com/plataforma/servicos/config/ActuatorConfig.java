package com.plataforma.servicos.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

// Configuração dos endpoints do Actuator
// Os endpoints são configurados no application.properties
// Essa classe existe para documentar a decisão
@Configuration
public class ActuatorConfig {
    // Configurações definidas no application.properties:
    // management.endpoints.web.exposure.include=health,info,metrics
    // management.endpoint.health.show-details=always
}
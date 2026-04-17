package com.plataforma.servicos.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// @RestController → combina @Controller + @ResponseBody
// Todos os métodos retornam JSON automaticamente
@RestController

// /api/v1/service-images → prefixo de todos os endpoints desta classe
@RequestMapping("/api/service-images")

// @RequiredArgsConstructor → injeta ServiceImageService via construtor
// Padrão recomendado pelo Spring — mais seguro que @Autowired
@RequiredArgsConstructor
public class ServiceImageController {
}

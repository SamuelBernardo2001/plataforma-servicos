package com.plataforma.servicos.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// @RestController → combina @Controller + @ResponseBody
// Indica que essa classe é um Controller REST
// Todos os métodos retornam JSON automaticamente
@RestController

// @RequestMapping → define o prefixo de todos os endpoints desta classe
// /api/v1/users → versionamento da API conforme documentação seção 8
@RequestMapping("/api/users")

// @RequiredArgsConstructor → injeta UserService via construtor
// Padrão recomendado pelo Spring — mais seguro que @Autowired
@RequiredArgsConstructor
public class UserController {
}

package com.plataforma.servicos.exception;

import org.springframework.web.bind.annotation.RestControllerAdvice;

// @RestControllerAdvice → intercepta exceções de todos os Controllers
// Centraliza o tratamento de erros em um único lugar
@RestControllerAdvice
public class GlobalExceptionHandler {
}

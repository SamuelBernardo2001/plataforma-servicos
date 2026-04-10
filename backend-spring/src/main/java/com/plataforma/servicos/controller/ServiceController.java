package com.plataforma.servicos.controller;

import com.plataforma.servicos.dto.ServiceDTOS.ServiceResponseDTO;
import com.plataforma.servicos.exception.ApiResponse;
import com.plataforma.servicos.service.ServicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// @RestController → combina @Controller + @ResponseBody
// Todos os métodos retornam JSON automaticamente
@RestController

// /api/services → prefixo de todos os endpoints desta classe
@RequestMapping("/api/services")

// @RequiredArgsConstructor → injeta ServiceService via construtor
// Padrão recomendado pelo Spring — mais seguro que @Autowired
@RequiredArgsConstructor
public class ServiceController {

    private final ServicoService serviceService;

    // CONSULTAS PÚBLICAS
    // Qualquer pessoa pode ver — sem autenticação

    // GET /api/services
    // Lista todos os serviços ATIVOS do marketplace
    // Regra: apenas serviços com ativo = true aparecem
    // Regra: filtro feito direto no banco via findByAtivo(true)
    // Quem usa: clientes buscando serviços, visitantes
    // No M7 não precisará de autenticação — endpoint público
    @GetMapping
    public ResponseEntity<ApiResponse<List<ServiceResponseDTO>>> findAll() {
        List<ServiceResponseDTO> services = serviceService.findAll();
        return ResponseEntity.ok(
                ApiResponse.success(
                        services,
                        "Serviços listados com sucesso",
                        HttpStatus.OK.value()
                ));
    }


}

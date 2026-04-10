package com.plataforma.servicos.controller;

import com.plataforma.servicos.dto.ServiceDTOS.ServiceResponseDTO;
import com.plataforma.servicos.exception.ApiResponse;
import com.plataforma.servicos.service.ServicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

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

    // GET /api/services/{id}
    // Busca um serviço específico pelo UUID
    // Regra: serviço desativado não é encontrado
    // Regra: retorna erro se UUID for inválido
    // Quem usa: cliente visualizando detalhes do serviço
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ServiceResponseDTO>> findById(
            @PathVariable UUID id) {
        ServiceResponseDTO service = serviceService.findById(id);
        return ResponseEntity.ok(
                ApiResponse.success(
                        service,
                        "Serviço encontrado",
                        HttpStatus.OK.value()
                ));
    }

    // GET /api/services/category/{categoriaId}
    // Lista serviços ATIVOS de uma categoria específica
    // Regra: categoria deve existir
    // Regra: apenas serviços ativos da categoria aparecem
    // Regra: filtro feito direto no banco via findByCategoriaIdAndAtivo()
    // Quem usa: cliente filtrando serviços por categoria
    @GetMapping("/category/{categoriaId}")
    public ResponseEntity<ApiResponse<List<ServiceResponseDTO>>> findByCategory(
            @PathVariable UUID categoriaId) {
        List<ServiceResponseDTO> services = serviceService.findByCategory(categoriaId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        services,
                        "Serviços da categoria listados com sucesso",
                        HttpStatus.OK.value()
                ));
    }

    // GET /api/services/prestador/{prestadorId}
    // Lista serviços ATIVOS de um prestador específico
    // Usado na página pública do prestador vista pelo cliente
    // Regra: apenas serviços ativos aparecem para o público
    // Regra: filtro via findByPrestadorIdAndAtivo()
    // Quem usa: cliente visitando perfil público do prestador
    @GetMapping("/prestador/{prestadorId}")
    public ResponseEntity<ApiResponse<List<ServiceResponseDTO>>> findByPrestador(
            @PathVariable UUID prestadorId) {
        List<ServiceResponseDTO> services = serviceService.findByPrestador(prestadorId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        services,
                        "Serviços do prestador listados com sucesso",
                        HttpStatus.OK.value()
                ));
    }

}

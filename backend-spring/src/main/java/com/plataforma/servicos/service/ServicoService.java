package com.plataforma.servicos.service;

import com.plataforma.servicos.dto.ServiceDTOS.ServiceResponseDTO;
import com.plataforma.servicos.entity.CategoryModel;
import com.plataforma.servicos.entity.ServiceModel;
import com.plataforma.servicos.mapper.ServiceMapper;
import com.plataforma.servicos.repository.CategoryRepository;
import com.plataforma.servicos.repository.ServiceRepository;
import com.plataforma.servicos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServicoService {

    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ServiceMapper serviceMapper;


    // Busca serviço por ID
    // Regra: serviço desativado não pode ser visualizado
    public ServiceResponseDTO findById(UUID id) {
        ServiceModel service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        if (Boolean.FALSE.equals(service.getAtivo())) {
            throw new RuntimeException("Serviço não está disponível");
        }

        return serviceMapper.toResponseDTO(service);
    }

    // Lista todos os serviços ativos
    // Regra: apenas serviços ativos aparecem na listagem pública
    public List<ServiceResponseDTO> findAll() {
        return serviceRepository.findAll()
                .stream()
                .filter(service -> Boolean.TRUE.equals(service.getAtivo()))
                .map(serviceMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Lista serviços por categoria
    // Regra: apenas serviços ativos da categoria aparecem
    public List<ServiceResponseDTO> findByCategory(UUID categoriaId) {
        CategoryModel categoria = categoryRepository.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        return serviceRepository.findAll()
                .stream()
                .filter(service -> Boolean.TRUE.equals(service.getAtivo()))
                .filter(service -> service.getCategoria() != null &&
                        service.getCategoria().getId().equals(categoriaId))
                .map(serviceMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}

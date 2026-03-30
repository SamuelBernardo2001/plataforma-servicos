package com.plataforma.servicos.service;

import com.plataforma.servicos.dto.ServiceDTOS.ServiceResponseDTO;
import com.plataforma.servicos.entity.ServiceModel;
import com.plataforma.servicos.mapper.ServiceMapper;
import com.plataforma.servicos.repository.CategoryRepository;
import com.plataforma.servicos.repository.ServiceRepository;
import com.plataforma.servicos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
}

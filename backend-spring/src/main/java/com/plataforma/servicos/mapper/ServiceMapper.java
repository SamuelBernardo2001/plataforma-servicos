package com.plataforma.servicos.mapper;

import com.plataforma.servicos.dto.ServiceDTOS.ServiceRequestDTO;
import com.plataforma.servicos.dto.ServiceDTOS.ServiceResponseDTO;
import com.plataforma.servicos.entity.ServiceModel;
import org.springframework.stereotype.Component;

@Component
public class ServiceMapper {

    // Converte ServiceModel para ServiceResponseDTO
    public ServiceResponseDTO toResponseDTO(ServiceModel service) {
        return new ServiceResponseDTO(
                service.getId(),
                service.getNome(),
                service.getDescricao(),
                service.getPreco(),
                service.getCategoria() != null ? service.getCategoria().getId() : null,
                service.getCategoria() != null ? service.getCategoria().getNome() : null,
                service.getPrestador() != null ? service.getPrestador().getId() : null,
                service.getPrestador() != null ? service.getPrestador().getNome() : null,
                service.getTelefoneContato(),
                service.getAtivo(),
                null, // mediaAvaliacao calculada no Service Layer
                service.getCriadoEm(),
                service.getAtualizadoEm()
        );
    }

    // Converte ServiceRequestDTO para ServiceModel
    public ServiceModel toModel(ServiceRequestDTO dto) {
        return ServiceModel.builder()
                .nome(dto.nome())
                .descricao(dto.descricao())
                .preco(dto.preco())
                .telefoneContato(dto.telefoneContato())
                .build();
        // prestador e categoria são setados no Service Layer
        // pois precisam ser buscados no banco pelo ID
    }
}
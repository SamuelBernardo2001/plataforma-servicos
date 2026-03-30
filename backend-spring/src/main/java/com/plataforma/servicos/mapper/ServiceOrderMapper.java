package com.plataforma.servicos.mapper;

import com.plataforma.servicos.dto.serviceOrderDTOS.ServiceOrderRequestDTO;
import com.plataforma.servicos.dto.serviceOrderDTOS.ServiceOrderResponseDTO;
import com.plataforma.servicos.entity.ServiceOrderModel;
import org.springframework.stereotype.Component;

@Component
public class ServiceOrderMapper {

    // Converte ServiceOrderModel para ServiceOrderResponseDTO
    public ServiceOrderResponseDTO toResponseDTO(ServiceOrderModel order) {
        return new ServiceOrderResponseDTO(
                order.getId(),
                order.getService() != null ? order.getService().getId() : null,
                order.getService() != null ? order.getService().getNome() : null,
                order.getCliente() != null ? order.getCliente().getId() : null,
                order.getCliente() != null ? order.getCliente().getNome() : null,
                order.getPrestador() != null ? order.getPrestador().getId() : null,
                order.getPrestador() != null ? order.getPrestador().getNome() : null,
                order.getDescricao(),
                order.getPrice(),
                order.getStatus(),
                order.getCriadoEm(),
                order.getAtualizadoEm(),
                order.getConcluidoEm()
        );
    }

    // Converte ServiceOrderRequestDTO para ServiceOrderModel
    public ServiceOrderModel toModel(ServiceOrderRequestDTO dto) {
        return ServiceOrderModel.builder()
                .descricao(dto.descricao())
                .price(dto.preco())
                .build();
        // service, cliente e prestador são setados no Service Layer
        // pois precisam ser buscados no banco pelo ID
    }
}
package com.plataforma.servicos.service;

import com.plataforma.servicos.dto.serviceOrderDTOS.ServiceOrderResponseDTO;
import com.plataforma.servicos.entity.ServiceOrderModel;
import com.plataforma.servicos.mapper.ServiceOrderMapper;
import com.plataforma.servicos.repository.ServiceOrderRepository;
import com.plataforma.servicos.repository.ServiceRepository;
import com.plataforma.servicos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ServiceOrderRepository serviceOrderRepository;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final ServiceOrderMapper serviceOrderMapper;

    // Busca ordem por ID
    // Regra: apenas cliente ou prestador da ordem podem visualizar
    public ServiceOrderResponseDTO findById(UUID id, UUID usuarioId) {
        ServiceOrderModel order = serviceOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ordem não encontrada"));

        if (!order.getCliente().getId().equals(usuarioId) &&
                !order.getPrestador().getId().equals(usuarioId)) {
            throw new RuntimeException("Você não tem permissão para visualizar esta ordem");
        }

        return serviceOrderMapper.toResponseDTO(order);
    }
}

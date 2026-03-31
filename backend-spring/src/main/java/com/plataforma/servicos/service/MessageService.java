package com.plataforma.servicos.service;

import com.plataforma.servicos.dto.messageDTOS.MessageResponseDTO;
import com.plataforma.servicos.entity.ServiceOrderModel;
import com.plataforma.servicos.mapper.MessageMapper;
import com.plataforma.servicos.repository.MessageRepository;
import com.plataforma.servicos.repository.ServiceOrderRepository;
import com.plataforma.servicos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ServiceOrderRepository serviceOrderRepository;
    private final UserRepository userRepository;
    private final MessageMapper messageMapper;

    // Lista mensagens de uma ordem com paginação
    // Igual ao WhatsApp — carrega as mensagens mais antigas primeiro
    // e vai carregando mais conforme o usuário sobe o scroll
    // Regra: apenas cliente ou prestador da ordem podem ver as mensagens
    // Regra: 20 mensagens por página — evita sobrecarga
    public Page<MessageResponseDTO> findByOrdem(UUID ordemId, UUID usuarioId, int pagina) {
        ServiceOrderModel ordem = serviceOrderRepository.findById(ordemId)
                .orElseThrow(() -> new RuntimeException("Ordem não encontrada"));

        // Apenas participantes da ordem veem as mensagens
        if (!ordem.getCliente().getId().equals(usuarioId) &&
                !ordem.getPrestador().getId().equals(usuarioId)) {
            throw new RuntimeException(
                    "Você não tem permissão para ver as mensagens desta ordem"
            );
        }

        // Paginação: 20 mensagens por página, ordenadas das mais antigas para mais recentes
        // Igual ao WhatsApp que mostra as mensagens em ordem cronológica
        PageRequest pageRequest = PageRequest.of(
                pagina,
                20, // 20 mensagens por página
                Sort.by("enviadoEm").ascending() // mais antigas primeiro
        );

        return messageRepository
                .findByServiceOrderIdOrderByEnviadoEmAsc(ordemId, pageRequest)
                .map(messageMapper::toResponseDTO);
    }

}

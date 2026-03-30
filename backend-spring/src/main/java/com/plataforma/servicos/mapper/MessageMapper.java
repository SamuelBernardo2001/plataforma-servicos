package com.plataforma.servicos.mapper;

import com.plataforma.servicos.dto.messageDTOS.MessageRequestDTO;
import com.plataforma.servicos.dto.messageDTOS.MessageResponseDTO;
import com.plataforma.servicos.entity.MessageModel;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    // Converte MessageModel para MessageResponseDTO
    public MessageResponseDTO toResponseDTO(MessageModel message) {
        return new MessageResponseDTO(
                message.getId(),
                message.getServiceOrder() != null ? message.getServiceOrder().getId() : null,
                message.getRemetente() != null ? message.getRemetente().getId() : null,
                message.getRemetente() != null ? message.getRemetente().getNome() : null,
                message.getReceptor() != null ? message.getReceptor().getId() : null,
                message.getReceptor() != null ? message.getReceptor().getNome() : null,
                message.getConteudo(),
                message.getLer(),
                message.getEnviadoEm()
        );
    }

    // Converte MessageRequestDTO para MessageModel
    public MessageModel toModel(MessageRequestDTO dto) {
        return MessageModel.builder()
                .conteudo(dto.conteudo())
                .ler(false) // mensagem sempre começa como não lida
                .build();
        // serviceOrder, remetente e receptor são setados no Service Layer
    }
}
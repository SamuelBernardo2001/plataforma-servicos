package com.plataforma.servicos.dto.messageDTOS;

import java.time.LocalDateTime;
import java.util.UUID;

public record MessageResponseDTO(

        UUID id,
        UUID serviceOrderId,
        UUID senderId,
        String senderNome,
        UUID receiverId,
        String receiverNome,
        String conteudo,
        Boolean lida,
        // Indica se mensagem foi editada — receptor sabe que foi modificada
        Boolean editado,
        // Data da edição — null se nunca foi editada
        LocalDateTime editadoEm,
        LocalDateTime enviadoEm

) { }
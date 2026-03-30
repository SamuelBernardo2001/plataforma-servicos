package com.plataforma.servicos.dto.MessageDTOS;

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
        LocalDateTime enviadoEm

) { }
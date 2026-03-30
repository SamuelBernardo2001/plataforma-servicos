package com.plataforma.servicos.dto.messageDTOS;

import jakarta.validation.constraints.*;
import java.util.UUID;

public record MessageRequestDTO(

        @NotNull(message = "Ordem de serviço é obrigatória")
        UUID serviceOrderId,

        @NotNull(message = "Destinatário é obrigatório")
        UUID receiverId,

        @NotBlank(message = "Conteúdo é obrigatório")
        @Size(min = 1, max = 1000, message = "Mensagem deve ter entre 1 e 1000 caracteres")
        String conteudo

) { }
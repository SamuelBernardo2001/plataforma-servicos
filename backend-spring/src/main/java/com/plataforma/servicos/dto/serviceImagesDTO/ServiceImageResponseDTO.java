package com.plataforma.servicos.dto.serviceImagesDTO;

import java.util.UUID;

public record ServiceImageResponseDTO(

        UUID id,

        // ID do serviço ao qual a imagem pertence
        UUID serviceId,

        // URL da imagem para exibição no frontend
        String url

) { }
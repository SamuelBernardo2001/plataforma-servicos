package com.plataforma.servicos.dto.favoriteDTOS;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Resposta com os dados do serviço favoritado pelo usuário")
public record FavoriteResponseDTO(

        @Schema(description = "ID do registro de favorito",
                example = "a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5p6")
        UUID id,

        @Schema(description = "ID do usuário que favoritou",
                example = "550e8400-e29b-41d4-a716-446655440000")
        UUID userId,

        @Schema(description = "ID do serviço favoritado",
                example = "722f9923-388e-473d-9d7a-111122223333")
        UUID serviceId,

        @Schema(description = "Título do serviço para exibição na lista de favoritos",
                example = "Limpeza de Estofados Premium")
        String serviceTitulo,

        @Schema(description = "Nome do prestador que oferece o serviço",
                example = "João da Silva")
        String prestadorNome,

        @Schema(description = "Data e hora em que o serviço foi favoritado")
        LocalDateTime criadoEm

) { }
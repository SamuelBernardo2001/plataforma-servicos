package com.plataforma.servicos.dto.ServiceDTOS;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Resposta detalhada contendo os dados do serviço, informações do prestador e métricas de avaliação")
public record ServiceResponseDTO(

        @Schema(description = "Identificador único do serviço (UUID)",
                example = "722f9923-388e-473d-9d7a-111122223333")
        UUID id,

        @Schema(description = "Título comercial do serviço",
                example = "Instalação de Ar Condicionado Split")
        String nome,

        @Schema(description = "Descrição detalhada do serviço",
                example = "Instalação completa até 12.000 BTUs, incluindo suporte e fiação básica.")
        String descricao,

        @Schema(description = "Preço base do serviço",
                example = "350.00")
        BigDecimal preco,

        @Schema(description = "ID da categoria vinculada",
                example = "550e8400-e29b-41d4-a716-446655440000")
        UUID categoriaId,

        @Schema(description = "Nome amigável da categoria para exibição",
                example = "Assistência Técnica")
        String categoriaNome,

        @Schema(description = "ID do prestador que oferece o serviço",
                example = "a1b2c3d4-e5f6-4372-a567-0e02b2c3d479")
        UUID prestadorId,

        @Schema(description = "Nome do prestador ou empresa",
                example = "João Silva Refrigeração")
        String prestadorNome,

        @Schema(description = "Contato direto do prestador",
                example = "11999998888")
        String telefoneContato,

        @Schema(description = "Indica se o serviço está visível e disponível para contratação",
                example = "true")
        Boolean ativo,

        @Schema(description = "Média de notas recebidas (0.0 a 5.0)",
                example = "4.8")
        Double mediaAvaliacao,

        @Schema(description = "Data e hora de criação do anúncio")
        LocalDateTime criadoEm,

        @Schema(description = "Data e hora da última atualização dos dados")
        LocalDateTime atualizadoEm

) { }
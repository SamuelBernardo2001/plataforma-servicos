package com.plataforma.servicos.dto.serviceOrderDTOS;

import com.plataforma.servicos.entity.OrderStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Resposta detalhada contendo o status e os envolvidos em uma ordem de serviço")
public record ServiceOrderResponseDTO(

        @Schema(description = "Identificador único da ordem de serviço (UUID)",
                example = "822f9923-388e-473d-9d7a-444455556666")
        UUID id,

        @Schema(description = "ID do serviço contratado",
                example = "722f9923-388e-473d-9d7a-111122223333")
        UUID serviceId,

        @Schema(description = "Título do serviço para exibição rápida",
                example = "Instalação de Ar Condicionado Split")
        String serviceTitulo,

        @Schema(description = "ID do cliente que solicitou o serviço",
                example = "550e8400-e29b-41d4-a716-446655440000")
        UUID clienteId,

        @Schema(description = "Nome do cliente solicitante",
                example = "Alice Souza")
        String clienteNome,

        @Schema(description = "ID do prestador responsável",
                example = "a1b2c3d4-e5f6-4372-a567-0e02b2c3d479")
        UUID prestadorId,

        @Schema(description = "Nome do prestador de serviço",
                example = "João Silva Refrigeração")
        String prestadorNome,

        @Schema(description = "Descrição detalhada ou observações da ordem",
                example = "Instalação para o sábado no período da manhã.")
        String descricao,

        @Schema(description = "Valor final acordado para a execução",
                example = "350.00")
        BigDecimal preco,

        @Schema(description = "Status atual da ordem (ex: SOLICITADO, ACEITO, CONCLUIDO, CANCELADO)",
                example = "ACEITO")
        OrderStatusEnum status,

        @Schema(description = "Data e hora de abertura da solicitação")
        LocalDateTime criadoEm,

        @Schema(description = "Data e hora da última movimentação de status")
        LocalDateTime atualizadoEm,

        @Schema(description = "Data e hora da finalização do serviço. Nulo se ainda não concluído.")
        LocalDateTime concluidoEm

) { }
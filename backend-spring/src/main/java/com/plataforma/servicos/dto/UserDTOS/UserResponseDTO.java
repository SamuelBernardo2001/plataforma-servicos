package com.plataforma.servicos.dto.UserDTOS;

import com.plataforma.servicos.dto.EnderecoDTOS.EnderecoResponseDTO;
import com.plataforma.servicos.entity.UserENUM;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Resposta detalhada contendo os dados do perfil do usuário e endereço vinculado")
public record UserResponseDTO(

        @Schema(description = "Identificador único do usuário (UUID)",
                example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "Nome completo do usuário",
                example = "Mariana Silva")
        String nome,

        @Schema(description = "Endereço de e-mail principal",
                example = "mariana.silva@email.com")
        String email,

        @Schema(description = "Telefone ou WhatsApp de contato",
                example = "11988887777")
        String telefone,

        @Schema(description = "Papel/Perfil do usuário no sistema",
                example = "CLIENTE")
        UserENUM perfil,

        @Schema(description = "Indica se a conta do usuário está ativa",
                example = "true")
        Boolean ativo,

        @Schema(description = "Dados detalhados do endereço vinculado ao usuário")
        EnderecoResponseDTO endereco,

        @Schema(description = "Data e hora do registro no sistema")
        LocalDateTime criadoEm,

        @Schema(description = "Data e hora da última alteração no perfil")
        LocalDateTime atualizadoEm

) { }
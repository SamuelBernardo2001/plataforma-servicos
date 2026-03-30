package com.plataforma.servicos.dto.UserDTOS;

import com.plataforma.servicos.dto.EnderecoDTOS.EnderecoResponseDTO;
import com.plataforma.servicos.entity.UserENUM;
import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponseDTO(

        UUID id,
        String nome,
        String email,
        String telefone,
        UserENUM perfil,
        Boolean ativo,
        EnderecoResponseDTO endereco,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm

) { }
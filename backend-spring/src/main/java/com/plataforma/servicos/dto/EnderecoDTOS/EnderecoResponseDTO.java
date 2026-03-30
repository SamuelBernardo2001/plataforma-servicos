package com.plataforma.servicos.dto.EnderecoDTOS;

import java.time.LocalDateTime;
import java.util.UUID;

public record EnderecoResponseDTO(

        UUID id,
        String cep,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String estado,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm

) { }
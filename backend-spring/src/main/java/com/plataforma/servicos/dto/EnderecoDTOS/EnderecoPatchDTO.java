package com.plataforma.servicos.dto.EnderecoDTOS;

import jakarta.validation.constraints.*;

public record EnderecoPatchDTO(

        // Todos os campos são opcionais — só atualiza o que for informado
        @Size(min = 8, max = 9, message = "CEP inválido")
        String cep,

        String logradouro,

        String numero,

        String complemento,

        String bairro,

        String cidade,

        @Size(min = 2, max = 2, message = "Estado deve ter 2 letras. Ex: PE, SP")
        String estado

) { }
package com.plataforma.servicos.dto.EnderecoDTOS;

import jakarta.validation.constraints.*;

public record EnderecoRequestDTO(

        @NotBlank(message = "CEP é obrigatório")
        @Size(min = 8, max = 9, message = "CEP inválido")
        String cep,

        @NotBlank(message = "Logradouro é obrigatório")
        String logradouro,

        @NotBlank(message = "Número é obrigatório")
        String numero,

        String complemento,

        @NotBlank(message = "Bairro é obrigatório")
        String bairro,

        @NotBlank(message = "Cidade é obrigatória")
        String cidade,

        @NotBlank(message = "Estado é obrigatório")
        @Size(min = 2, max = 2, message = "Estado deve ter 2 letras. Ex: PE, SP")
        String estado

) { }
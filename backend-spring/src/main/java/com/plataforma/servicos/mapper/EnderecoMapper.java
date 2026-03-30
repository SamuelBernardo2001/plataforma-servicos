package com.plataforma.servicos.mapper;

import com.plataforma.servicos.dto.EnderecoDTOS.EnderecoRequestDTO;
import com.plataforma.servicos.dto.EnderecoDTOS.EnderecoResponseDTO;
import com.plataforma.servicos.entity.EnderecoModel;
import org.springframework.stereotype.Component;

@Component
public class EnderecoMapper {

    // Converte EnderecoModel para EnderecoResponseDTO
    public EnderecoResponseDTO toResponseDTO(EnderecoModel endereco) {
        return new EnderecoResponseDTO(
                endereco.getId(),
                endereco.getCep(),
                endereco.getLogradouro(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getBairro(),
                endereco.getCidade(),
                endereco.getEstado(),
                endereco.getCriadoEm(),
                endereco.getAtualizadoEm()
        );
    }

    // Converte EnderecoRequestDTO para EnderecoModel
    public EnderecoModel toModel(EnderecoRequestDTO dto) {
        return EnderecoModel.builder()
                .cep(dto.cep())
                .logradouro(dto.logradouro())
                .numero(dto.numero())
                .complemento(dto.complemento())
                .bairro(dto.bairro())
                .cidade(dto.cidade())
                .estado(dto.estado())
                .build();
        // user é setado no Service Layer
    }
}
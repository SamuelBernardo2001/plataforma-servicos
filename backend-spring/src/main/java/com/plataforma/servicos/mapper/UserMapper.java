package com.plataforma.servicos.mapper;

import com.plataforma.servicos.dto.UserDTOS.UserRequestDTO;
import com.plataforma.servicos.dto.UserDTOS.UserResponseDTO;
import com.plataforma.servicos.entity.UserModel;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    // Converte UserModel para UserResponseDTO
    public UserResponseDTO toResponseDTO(UserModel user) {
        return new UserResponseDTO(
                user.getId(),
                user.getNome(),
                user.getEmail(),
                user.getTelefone(),
                user.getPerfil(),
                user.getAtivo(),
                null, // endereço carregado separadamente via EnderecoService
                user.getCriadoEm(),
                user.getAtualizadoEm()
        );
    }

    // Converte UserRequestDTO para UserModel
    public UserModel toModel(UserRequestDTO dto) {
        return UserModel.builder()
                .nome(dto.nome())
                .email(dto.email())
                .senha(dto.senha()) // será criptografada no M7 (Segurança)
                .telefone(dto.telefone())
                .perfil(dto.perfil())
                .build();
    }
}
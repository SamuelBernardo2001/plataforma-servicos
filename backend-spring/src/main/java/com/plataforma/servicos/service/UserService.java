package com.plataforma.servicos.service;

import com.plataforma.servicos.dto.UserDTOS.UserResponseDTO;
import com.plataforma.servicos.entity.UserModel;
import com.plataforma.servicos.mapper.UserMapper;
import com.plataforma.servicos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository; // Injeção do UserRepository para acessar o banco de dados
    private final UserMapper userMapper; // Injeção do UserMapper para converter entre UserModel e UserResponseDTO

    // Busca usuário por ID
    public UserResponseDTO findById(UUID id) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return userMapper.toResponseDTO(user);
    }

    // Busca usuário por email
    public UserResponseDTO findByEmail(String email) {
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return userMapper.toResponseDTO(user);
    }

    }


package com.plataforma.servicos.service;

import com.plataforma.servicos.dto.UserDTOS.UserRequestDTO;
import com.plataforma.servicos.dto.UserDTOS.UserResponseDTO;
import com.plataforma.servicos.entity.UserModel;
import com.plataforma.servicos.mapper.UserMapper;
import com.plataforma.servicos.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    // Lista todos os usuários ativos
    public List<UserResponseDTO> findAll() {
        return userRepository.findAll()
                .stream()
                .filter(user -> Boolean.TRUE.equals(user.getAtivo()))
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Cria novo usuário
    // Regra: email único no sistema
    @Transactional
    public UserResponseDTO create(UserRequestDTO dto) {
        if (userRepository.findByEmail(dto.email()).isPresent()) {
            throw new RuntimeException("Email já cadastrado no sistema");
        }

        UserModel user = userMapper.toModel(dto);
        user.setCriadoEm(LocalDateTime.now());
        user.setAtualizadoEm(LocalDateTime.now());

        return userMapper.toResponseDTO(userRepository.save(user));
    }

    }


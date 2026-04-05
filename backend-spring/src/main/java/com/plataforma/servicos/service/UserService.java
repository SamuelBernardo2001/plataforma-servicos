package com.plataforma.servicos.service;

import com.plataforma.servicos.dto.UserDTOS.*;
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

    // Atualiza dados do perfil do usuário
    // Regra: apenas nome e telefone podem ser alterados
    @Transactional
    public UserResponseDTO update(UUID id, UserUpdateDTO dto) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        user.setNome(dto.nome());
        user.setTelefone(dto.telefone());
        user.setAtualizadoEm(LocalDateTime.now());

        return userMapper.toResponseDTO(userRepository.save(user));
    }

    // Atualiza senha do usuário
    // Regra: senha atual deve ser confirmada antes de alterar
    // Regra: nova senha deve ser diferente da atual
    // Regra: nova senha e confirmação devem ser iguais
    @Transactional
    public void updatePassword(UUID id, UserPasswordDTO dto) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!user.getSenha().equals(dto.senhaAtual())) {
            throw new RuntimeException("Senha atual incorreta");
        }

        if (!dto.novaSenha().equals(dto.confirmarSenha())) {
            throw new RuntimeException("Nova senha e confirmação não coincidem");
        }

        if (dto.novaSenha().equals(dto.senhaAtual())) {
            throw new RuntimeException("Nova senha deve ser diferente da senha atual");
        }

        user.setSenha(dto.novaSenha()); // será criptografada no M7 (Segurança)
        user.setAtualizadoEm(LocalDateTime.now());

        userRepository.save(user);
    }


    // Desativa usuário (soft delete)
    // Regra: usuário não é deletado do banco, apenas desativado
    @Transactional
    public void deactivate(UUID id) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (Boolean.FALSE.equals(user.getAtivo())) {
            throw new RuntimeException("Usuário já está desativado");
        }

        user.setAtivo(false);
        user.setAtualizadoEm(LocalDateTime.now());

        userRepository.save(user);
    }

    // Autentica usuário com email e senha
// Regra: email deve existir no sistema
// Regra: senha deve ser igual à cadastrada
// Regra: usuário deve estar ativo
// No M7 a senha será comparada com BCrypt
    public UserResponseDTO login(UserLoginDTO dto) {
        UserModel user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new RuntimeException("Email ou senha inválidos"));

        // Valida se usuário está ativo
        if (Boolean.FALSE.equals(user.getAtivo())) {
            throw new RuntimeException("Usuário inativo — entre em contato com o suporte");
        }

        // Valida senha — no M7 será substituído por BCrypt
        // passwordEncoder.matches(dto.senha(), user.getSenha())
        if (!user.getSenha().equals(dto.senha())) {
            throw new RuntimeException("Email ou senha inválidos");
        }

        return userMapper.toResponseDTO(user);
    }
    }


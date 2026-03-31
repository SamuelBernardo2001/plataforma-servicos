package com.plataforma.servicos.service;

import com.plataforma.servicos.dto.EnderecoDTOS.EnderecoResponseDTO;
import com.plataforma.servicos.entity.EnderecoModel;
import com.plataforma.servicos.mapper.EnderecoMapper;
import com.plataforma.servicos.repository.EnderecoRepository;
import com.plataforma.servicos.repository.ServiceOrderRepository;
import com.plataforma.servicos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;
    private final UserRepository userRepository;
    private final ServiceOrderRepository serviceOrderRepository;
    private final EnderecoMapper enderecoMapper;

    // Busca endereço do próprio usuário
    // Regra: usuário só pode ver seu próprio endereço
    public EnderecoResponseDTO findByUsuario(UUID usuarioId) {
        userRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        EnderecoModel endereco = enderecoRepository.findByUserId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Endereço não cadastrado"));

        return enderecoMapper.toResponseDTO(endereco);
    }
}

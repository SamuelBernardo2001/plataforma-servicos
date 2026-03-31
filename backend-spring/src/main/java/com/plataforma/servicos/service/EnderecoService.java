package com.plataforma.servicos.service;

import com.plataforma.servicos.dto.EnderecoDTOS.EnderecoRequestDTO;
import com.plataforma.servicos.dto.EnderecoDTOS.EnderecoResponseDTO;
import com.plataforma.servicos.entity.EnderecoModel;
import com.plataforma.servicos.entity.OrderStatusEnum;
import com.plataforma.servicos.entity.UserModel;
import com.plataforma.servicos.mapper.EnderecoMapper;
import com.plataforma.servicos.repository.EnderecoRepository;
import com.plataforma.servicos.repository.ServiceOrderRepository;
import com.plataforma.servicos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    // Prestador visualiza endereço do cliente
    // Regra: prestador só consegue ver o endereço se existir ordem ACCEPTED
    //        entre ele e o cliente — endereço some após COMPLETED
    // Regra: protege privacidade do cliente
    public EnderecoResponseDTO findByUsuarioParaPrestador(UUID clienteId, UUID prestadorId) {
        // Verifica se existe ordem ACCEPTED entre prestador e cliente
        // Só com ordem aceita o prestador pode ver o endereço
        boolean temOrdemAceita = serviceOrderRepository
                .findByClienteIdAndPrestadorId(clienteId, prestadorId)
                .stream()
                .anyMatch(o -> OrderStatusEnum.ACCEPTED.equals(o.getStatus()));

        // Se não tem ordem ACCEPTED o endereço não é visível
        if (!temOrdemAceita) {
            throw new RuntimeException(
                    "Endereço não disponível — só é visível após ordem aceita e antes de ser concluída"
            );
        }

        EnderecoModel endereco = enderecoRepository.findByUserId(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não possui endereço cadastrado"));

        return enderecoMapper.toResponseDTO(endereco);
    }

    // Cadastra endereço após criação da conta
    // Regra: usuário deve estar ativo
    // Regra: usuário só pode ter um endereço — unicidade
    @Transactional
    public EnderecoResponseDTO create(UUID usuarioId, EnderecoRequestDTO dto) {
        UserModel usuario = userRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (Boolean.FALSE.equals(usuario.getAtivo())) {
            throw new RuntimeException("Usuário inativo não pode cadastrar endereço");
        }

        // Unicidade — um usuário só pode ter um endereço cadastrado
        if (enderecoRepository.findByUserId(usuarioId).isPresent()) {
            throw new RuntimeException(
                    "Usuário já possui endereço cadastrado — use a opção de editar"
            );
        }

        EnderecoModel endereco = enderecoMapper.toModel(dto);
        endereco.setUser(usuario);
        endereco.setCriadoEm(LocalDateTime.now());
        endereco.setAtualizadoEm(LocalDateTime.now());

        return enderecoMapper.toResponseDTO(enderecoRepository.save(endereco));
    }
}

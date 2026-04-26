package com.plataforma.servicos.service;

import com.plataforma.servicos.dto.EnderecoDTOS.EnderecoPatchDTO;
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

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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

        return enderecoMapper.toResponseDTO(enderecoRepository.save(endereco));
    }

    // Atualiza endereço completo do usuário
    // Regra: usuário só pode atualizar seu próprio endereço
    // Regra: todos os campos são atualizados de uma vez
    @Transactional
    public EnderecoResponseDTO update(UUID usuarioId, EnderecoRequestDTO dto) {
        userRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        EnderecoModel endereco = enderecoRepository.findByUserId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Endereço não cadastrado"));

        // Atualiza todos os campos de uma vez
        endereco.setCep(dto.cep());
        endereco.setLogradouro(dto.logradouro());
        endereco.setNumero(dto.numero());
        endereco.setComplemento(dto.complemento());
        endereco.setBairro(dto.bairro());
        endereco.setCidade(dto.cidade());
        endereco.setEstado(dto.estado());

        return enderecoMapper.toResponseDTO(enderecoRepository.save(endereco));
    }

    // Atualiza campos individuais do endereço (edição parcial)
    // Regra: usuário pode atualizar apenas um campo por vez
    // Regra: campos não informados (null) não são alterados
    // Por que isso? Usuário não precisa redigitar tudo para mudar apenas o número
    @Transactional
    public EnderecoResponseDTO patch(UUID usuarioId, EnderecoPatchDTO dto) {
        userRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        EnderecoModel endereco = enderecoRepository.findByUserId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Endereço não cadastrado"));

        // Atualiza apenas os campos que foram informados (não nulos)
        if (dto.cep() != null) endereco.setCep(dto.cep());
        if (dto.logradouro() != null) endereco.setLogradouro(dto.logradouro());
        if (dto.numero() != null) endereco.setNumero(dto.numero());
        if (dto.complemento() != null) endereco.setComplemento(dto.complemento());
        if (dto.bairro() != null) endereco.setBairro(dto.bairro());
        if (dto.cidade() != null) endereco.setCidade(dto.cidade());
        if (dto.estado() != null) endereco.setEstado(dto.estado());

        return enderecoMapper.toResponseDTO(enderecoRepository.save(endereco));
    }

    // Deleta endereço do usuário
    // Regra: usuário só pode deletar seu próprio endereço
    @Transactional
    public void delete(UUID usuarioId) {
        userRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        EnderecoModel endereco = enderecoRepository.findByUserId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Endereço não cadastrado"));

        enderecoRepository.delete(endereco);
    }
}
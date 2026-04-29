package com.plataforma.servicos.service;

import com.plataforma.servicos.dto.UserDTOS.*;
import com.plataforma.servicos.entity.UserModel;
import com.plataforma.servicos.mapper.UserMapper;
import com.plataforma.servicos.repository.UserRepository;
import com.plataforma.servicos.util.PaginatedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional; // Ajustado para Spring Transaction
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // Mantém a sessão ativa para todos os métodos de busca
public class UserService {

    private final UserRepository userRepository; // Injeção do UserRepository para acessar o banco de dados
    private final UserMapper userMapper; // Injeção do UserMapper para converter entre UserModel e UserResponseDTO
    private static final Logger log = LoggerFactory.getLogger(UserService.class); // Logger para registrar informações e erros do serviço

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

    // Lista todos os usuários ativos com paginação
    // Regra: apenas usuários com ativo = true aparecem
    // Filtro feito direto no banco via findByAtivo()
    public PaginatedResponse<UserResponseDTO> findAll(Pageable pageable) {
        Page<UserResponseDTO> page = userRepository
                .findByAtivo(true, pageable)
                .map(userMapper::toResponseDTO);
        return PaginatedResponse.of(page);
    }

    /**
     * Cria um novo usuário no sistema.
     * * O processo segue este fluxo:
     * 1. Validação: Verifica se o e-mail já existe para garantir a unicidade.
     * 2. Mapeamento: Converte o DTO de entrada para a Entidade (UserModel).
     * 3. Auditoria Automática: Ao salvar, o Spring Data JPA (via BaseEntity) preenche
     * automaticamente os campos 'criadoEm', 'atualizadoEm', 'criadoPor' e 'atualizadoPor',
     * eliminando a necessidade de setar datas manualmente no código.
     * 4. Persistência: O usuário é salvo no MySQL e o ID é gerado.
     */
    @Transactional
    public UserResponseDTO create(UserRequestDTO dto) {
        log.info("Tentativa de cadastro de usuario com email: {}", dto.email());

        if (userRepository.findByEmail(dto.email()).isPresent()) {
            log.warn("Email ja cadastrado no sistema: {}", dto.email());
            throw new RuntimeException("Email ja cadastrado no sistema");
        }

        UserModel user = userMapper.toModel(dto);
        UserResponseDTO response = userMapper.toResponseDTO(userRepository.save(user));
        log.info("Usuario cadastrado com sucesso — id: {}, perfil: {}", response.id(), response.perfil());
        return response;
    }

    // Atualiza dados do perfil do usuário
    // Regra: apenas nome e telefone podem ser alterados
    @Transactional
    public UserResponseDTO update(UUID id, UserUpdateDTO dto) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        user.setNome(dto.nome());
        user.setTelefone(dto.telefone());

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

        userRepository.save(user);
    }


    /**
     * Realiza a desativação lógica de um usuário no sistema.
     * * O processo segue este fluxo:
     * 1. Localização: Busca o usuário pelo UUID. Caso não exista, interrompe o processo
     * lançando uma exceção.
     * 2. Validação de Estado (Idempotência): Verifica se o usuário já está inativo.
     * Isso evita processamento desnecessário e informa ao sistema que a operação já foi realizada.
     * 3. Soft Delete (Exclusão Lógica): Em vez de apagar o registro, altera o status 'ativo' para false.
     * Isso preserva o histórico de auditoria e a integridade referencial.
     * 4. Persistência e Auditoria: O comando save() dispara o 'AuditingEntityListener' (BaseEntity),
     * atualizando automaticamente a coluna 'atualizado_em' e 'atualizado_por'.
     */
    @Transactional
    public void deactivate(UUID id) {
        log.info("Desativando usuario — id: {}", id);

        // Garante que o ID fornecido corresponde a um usuário real
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));

        // Regra de Negócio: Impede a redundância de desativar quem já está inativo
        if (Boolean.FALSE.equals(user.getAtivo())) {
            log.warn("Usuario ja estava desativado — id: {}", id);
            throw new RuntimeException("Usuario ja esta desativado");
        }

        // Altera apenas o campo necessário (Soft Delete)
        user.setAtivo(false);

        // O @Transactional garante que a alteração seja persistida com segurança
        userRepository.save(user);

        log.info("Usuario desativado com sucesso — id: {}", id);
    }

    /**
     * Realiza a autenticação de um usuário no sistema.
     * * O processo segue este fluxo:
     * 1. Rastreabilidade: Registra o início da tentativa de login para monitoramento.
     * 2. Busca e Validação de Existência: Procura o usuário pelo e-mail. Caso não exista,
     * lança uma exceção genérica para evitar revelar quais e-mails estão na base (segurança).
     * 3. Validação de Credenciais: Compara a senha fornecida com a senha armazenada.
     * NOTA: Atualmente usa comparação simples, mas será atualizado para BCrypt na Etapa 7.
     * 4. Mapeamento e Resposta: Após o sucesso, converte a entidade para DTO e registra
     * o log de sucesso com o ID do usuário.
     */
    public UserResponseDTO login(UserLoginDTO dto) {
        log.info("Tentativa de login para email: {}", dto.email());

        // Busca o usuário e registra aviso se o e-mail for inexistente
        UserModel user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> {
                    log.warn("Login falhou — email nao encontrado: {}", dto.email());
                    return new RuntimeException("Email ou senha invalidos");
                });

        // Validação de senha com log de erro específico para administração
        if (!user.getSenha().equals(dto.senha())) {
            log.warn("Login falhou — senha incorreta para email: {}", dto.email());
            throw new RuntimeException("Email ou senha invalidos");
        }

        log.info("Login realizado com sucesso — userId: {}", user.getId());

        return userMapper.toResponseDTO(user);
    }
}
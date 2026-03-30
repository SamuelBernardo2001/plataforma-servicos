package com.plataforma.servicos.service;

import com.plataforma.servicos.dto.ServiceDTOS.ServiceRequestDTO;
import com.plataforma.servicos.dto.ServiceDTOS.ServiceResponseDTO;
import com.plataforma.servicos.entity.CategoryModel;
import com.plataforma.servicos.entity.ServiceModel;
import com.plataforma.servicos.entity.UserENUM;
import com.plataforma.servicos.entity.UserModel;
import com.plataforma.servicos.mapper.ServiceMapper;
import com.plataforma.servicos.repository.CategoryRepository;
import com.plataforma.servicos.repository.ServiceRepository;
import com.plataforma.servicos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServicoService {

    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ServiceMapper serviceMapper;


    // Busca serviço por ID
    // Regra: serviço desativado não pode ser visualizado
    public ServiceResponseDTO findById(UUID id) {
        ServiceModel service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        if (Boolean.FALSE.equals(service.getAtivo())) {
            throw new RuntimeException("Serviço não está disponível");
        }

        return serviceMapper.toResponseDTO(service);
    }

    // Lista todos os serviços ativos
    // Regra: apenas serviços ativos aparecem na listagem pública
    // Usa findByAtivo() — filtra direto no banco: WHERE ativo = true
    public List<ServiceResponseDTO> findAll() {
        return serviceRepository.findByAtivo(true)
                .stream()
                .map(serviceMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Lista serviços ativos por categoria
    // Regra: categoria deve existir
    // Regra: apenas serviços ativos da categoria aparecem
    // Usa findByCategoriaIdAndAtivo() — filtra direto no banco:
    // WHERE categoria_id = ? AND ativo = true
    public List<ServiceResponseDTO> findByCategory(UUID categoriaId) {
        categoryRepository.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        return serviceRepository.findByCategoriaIdAndAtivo(categoriaId, true)
                .stream()
                .map(serviceMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Lista serviços ativos de um prestador específico
    // Usado na página pública do prestador vista pelo cliente
    // Regra: prestador deve existir
    // Regra: apenas serviços ativos aparecem para o público
    // Usa findByPrestadorIdAndAtivo() — filtra direto no banco
    public List<ServiceResponseDTO> findByPrestador(UUID prestadorId) {
        userRepository.findById(prestadorId)
                .orElseThrow(() -> new RuntimeException("Prestador não encontrado"));

        return serviceRepository.findByPrestadorIdAndAtivo(prestadorId, true)
                .stream()
                .map(serviceMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Lista TODOS os serviços de um prestador (ativos e inativos)
    // Usado no painel do prestador para gerenciar seus próprios serviços
    // Regra: prestador deve existir
    // Regra: prestador vê todos os seus serviços inclusive os desativados
    // Usa findByPrestadorId() — filtra direto no banco:
    public List<ServiceResponseDTO> findAllByPrestador(UUID prestadorId) {
        userRepository.findById(prestadorId)
                .orElseThrow(() -> new RuntimeException("Prestador não encontrado"));

        return serviceRepository.findByPrestadorId(prestadorId)
                .stream()
                .map(serviceMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Cria novo serviço
    // Regra: apenas usuários com perfil PRESTADOR podem criar serviços
    // Regra: categoria informada deve existir e estar ativa
    // Regra: prestador deve estar ativo no sistema
    @Transactional
    public ServiceResponseDTO create(UUID prestadorId, ServiceRequestDTO dto) {
        UserModel prestador = userRepository.findById(prestadorId)
                .orElseThrow(() -> new RuntimeException("Prestador não encontrado"));

        if (!UserENUM.PRESTADOR.equals(prestador.getPerfil())) {
            throw new RuntimeException("Apenas prestadores podem cadastrar serviços");
        }

        if (Boolean.FALSE.equals(prestador.getAtivo())) {
            throw new RuntimeException("Prestador inativo não pode cadastrar serviços");
        }

        CategoryModel categoria = categoryRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        if (Boolean.FALSE.equals(categoria.getAtivo())) {
            throw new RuntimeException("Categoria inativa não pode ser utilizada");
        }

        ServiceModel service = serviceMapper.toModel(dto);
        service.setPrestador(prestador);
        service.setCategoria(categoria);
        service.setCriadoEm(LocalDateTime.now());
        service.setAtualizadoEm(LocalDateTime.now());

        return serviceMapper.toResponseDTO(serviceRepository.save(service));
    }

    // Atualiza dados do serviço
    // Regra: apenas o próprio prestador pode atualizar seu serviço
    // Regra: não é possível atualizar serviço desativado
    @Transactional
    public ServiceResponseDTO update(UUID id, UUID prestadorId, ServiceRequestDTO dto) {
        ServiceModel service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        if (!service.getPrestador().getId().equals(prestadorId)) {
            throw new RuntimeException("Você não tem permissão para atualizar este serviço");
        }

        if (Boolean.FALSE.equals(service.getAtivo())) {
            throw new RuntimeException("Não é possível atualizar um serviço desativado");
        }

        CategoryModel categoria = categoryRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        if (Boolean.FALSE.equals(categoria.getAtivo())) {
            throw new RuntimeException("Categoria inativa não pode ser utilizada");
        }

        service.setNome(dto.nome());
        service.setDescricao(dto.descricao());
        service.setPreco(dto.preco());
        service.setTelefoneContato(dto.telefoneContato());
        service.setCategoria(categoria);
        service.setAtualizadoEm(LocalDateTime.now());

        return serviceMapper.toResponseDTO(serviceRepository.save(service));
    }

    // Desativa serviço (soft delete)
    // Regra: apenas o próprio prestador pode desativar seu serviço
    // Regra: serviço já desativado não pode ser desativado novamente
    @Transactional
    public void deactivate(UUID id, UUID prestadorId) {
        ServiceModel service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        if (!service.getPrestador().getId().equals(prestadorId)) {
            throw new RuntimeException("Você não tem permissão para desativar este serviço");
        }

        if (Boolean.FALSE.equals(service.getAtivo())) {
            throw new RuntimeException("Serviço já está desativado");
        }

        service.setAtivo(false);
        service.setAtualizadoEm(LocalDateTime.now());

        serviceRepository.save(service);
    }

    // Calcula e retorna a média de avaliações de um serviço
    // Regra: média calculada apenas com avaliações existentes
    // Regra: se não houver avaliações retorna 0.0
    public Double calcularMediaAvaliacao(UUID id) {
        ServiceModel service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        if (service.getAvaliacoes() == null || service.getAvaliacoes().isEmpty()) {
            return 0.0;
        }

        return service.getAvaliacoes()
                .stream()
                .mapToInt(review -> review.getClassificacao())
                .average()
                .orElse(0.0);
    }


}

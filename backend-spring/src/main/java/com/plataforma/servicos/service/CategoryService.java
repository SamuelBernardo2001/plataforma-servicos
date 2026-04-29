package com.plataforma.servicos.service;

import com.plataforma.servicos.dto.CategoryDTOS.CategoryRequestDTO;
import com.plataforma.servicos.dto.CategoryDTOS.CategoryResponseDTO;
import com.plataforma.servicos.entity.CategoryModel;
import com.plataforma.servicos.entity.UserENUM;
import com.plataforma.servicos.entity.UserModel;
import com.plataforma.servicos.mapper.CategoryMapper;
import com.plataforma.servicos.repository.CategoryRepository;
import com.plataforma.servicos.repository.UserRepository;
import com.plataforma.servicos.util.PaginatedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CategoryMapper categoryMapper;

    // Lista todas as categorias ativas
    // Regra: qualquer pessoa pode ver as categorias — são públicas
    // Usa findByAtivo() — filtra direto no banco: WHERE ativo = true
    // Lista todas as categorias ativas com paginação
    // Regra: apenas categorias com ativo = true aparecem
    // Filtro direto no banco via findByAtivo(true)
    public PaginatedResponse<CategoryResponseDTO> findAll(Pageable pageable) {
        Page<CategoryResponseDTO> page = categoryRepository
                .findAll(pageable) // @Where garante ativo = true
                .map(categoryMapper::toResponseDTO);
        return PaginatedResponse.of(page);
    }

    // Busca categoria por ID
    // Regra: qualquer pessoa pode buscar uma categoria
    public CategoryResponseDTO findById(UUID id) {
        CategoryModel category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        return categoryMapper.toResponseDTO(category);
    }

    // Cria nova categoria
    // Regra: apenas ADMIN pode criar categoria
    // Regra: nome da categoria deve ser único no sistema
    @Transactional
    public CategoryResponseDTO create(UUID adminId, CategoryRequestDTO dto) {
        UserModel admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Apenas ADMIN pode criar categorias — controle de moderação
        if (!UserENUM.ADMIN.equals(admin.getPerfil())) {
            throw new RuntimeException("Apenas administradores podem criar categorias");
        }

        // Nome único — não pode ter 2 categorias com o mesmo nome
        if (categoryRepository.findByNome(dto.nome()).isPresent()) {
            throw new RuntimeException("Já existe uma categoria com esse nome");
        }

        CategoryModel category = categoryMapper.toModel(dto);

        return categoryMapper.toResponseDTO(categoryRepository.save(category));
    }

    // Edita nome ou descrição de uma categoria
    // Regra: apenas ADMIN pode editar categoria
    // Regra: novo nome deve ser único se for diferente do atual
    @Transactional
    public CategoryResponseDTO update(UUID id, UUID adminId, CategoryRequestDTO dto) {
        UserModel admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!UserENUM.ADMIN.equals(admin.getPerfil())) {
            throw new RuntimeException("Apenas administradores podem editar categorias");
        }

        CategoryModel category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        // Verifica unicidade do nome apenas se foi alterado
        if (!category.getNome().equals(dto.nome()) &&
                categoryRepository.findByNome(dto.nome()).isPresent()) {
            throw new RuntimeException("Já existe uma categoria com esse nome");
        }

        category.setNome(dto.nome());
        category.setDescricao(dto.descricao());

        return categoryMapper.toResponseDTO(categoryRepository.save(category));
    }

    // Desativa categoria (soft delete)
    // Regra: apenas ADMIN pode desativar categoria
    // Regra: categoria já desativada não pode ser desativada novamente
    // Regra: serviços que usavam essa categoria ficam pendentes —
    //        prestador precisará cadastrar uma nova categoria para o serviço
    @Transactional
    public void deactivate(UUID id, UUID adminId) {
        UserModel admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!UserENUM.ADMIN.equals(admin.getPerfil())) {
            throw new RuntimeException("Apenas administradores podem desativar categorias");
        }

        CategoryModel category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        if (Boolean.FALSE.equals(category.getAtivo())) {
            throw new RuntimeException("Categoria já está desativada");
        }

        // Desativa a categoria — serviços vinculados ficam pendentes
        // O prestador receberá notificação para atualizar a categoria
        // do seu serviço (será implementado no M8 — Observabilidade)
        category.setAtivo(false);

        categoryRepository.save(category);
    }
}
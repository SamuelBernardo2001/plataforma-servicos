package com.plataforma.servicos.service;

import com.plataforma.servicos.dto.CategoryDTOS.CategoryRequestDTO;
import com.plataforma.servicos.dto.CategoryDTOS.CategoryResponseDTO;
import com.plataforma.servicos.entity.CategoryModel;
import com.plataforma.servicos.entity.UserENUM;
import com.plataforma.servicos.entity.UserModel;
import com.plataforma.servicos.mapper.CategoryMapper;
import com.plataforma.servicos.repository.CategoryRepository;
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
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CategoryMapper categoryMapper;

    // Lista todas as categorias ativas
    // Regra: qualquer pessoa pode ver as categorias — são públicas
    // Usa findByAtivo() — filtra direto no banco: WHERE ativo = true
    public List<CategoryResponseDTO> findAll() {
        return categoryRepository.findByAtivo(true)
                .stream()
                .map(categoryMapper::toResponseDTO)
                .collect(Collectors.toList());
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
        category.setCriadoEm(LocalDateTime.now());
        category.setAtualizadoEm(LocalDateTime.now());

        return categoryMapper.toResponseDTO(categoryRepository.save(category));
    }
}

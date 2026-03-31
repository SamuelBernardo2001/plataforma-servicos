package com.plataforma.servicos.service;

import com.plataforma.servicos.dto.CategoryDTOS.CategoryResponseDTO;
import com.plataforma.servicos.mapper.CategoryMapper;
import com.plataforma.servicos.repository.CategoryRepository;
import com.plataforma.servicos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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
}

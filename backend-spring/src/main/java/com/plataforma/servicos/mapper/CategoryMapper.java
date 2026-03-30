package com.plataforma.servicos.mapper;

import com.plataforma.servicos.dto.CategoryDTOS.CategoryRequestDTO;
import com.plataforma.servicos.dto.CategoryDTOS.CategoryResponseDTO;
import com.plataforma.servicos.entity.CategoryModel;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    // Converte CategoryModel para CategoryResponseDTO
    public CategoryResponseDTO toResponseDTO(CategoryModel category) {
        return new CategoryResponseDTO(
                category.getId(),
                category.getNome(),
                category.getDescricao(),
                category.getAtivo(),
                category.getCriadoEm(),
                category.getAtualizadoEm()
        );
    }

    // Converte CategoryRequestDTO para CategoryModel
    public CategoryModel toModel(CategoryRequestDTO dto) {
        return CategoryModel.builder()
                .nome(dto.nome())
                .descricao(dto.descricao())
                .build();
    }
}
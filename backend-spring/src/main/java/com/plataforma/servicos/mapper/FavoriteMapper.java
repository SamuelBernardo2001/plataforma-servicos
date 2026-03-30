package com.plataforma.servicos.mapper;

import com.plataforma.servicos.dto.favoriteDTOS.FavoriteRequestDTO;
import com.plataforma.servicos.dto.favoriteDTOS.FavoriteResponseDTO;
import com.plataforma.servicos.entity.FavoriteModel;
import org.springframework.stereotype.Component;

@Component
public class FavoriteMapper {

    // Converte FavoriteModel para FavoriteResponseDTO
    public FavoriteResponseDTO toResponseDTO(FavoriteModel favorite) {
        return new FavoriteResponseDTO(
                favorite.getId(),
                favorite.getUsuario() != null ? favorite.getUsuario().getId() : null,
                favorite.getService() != null ? favorite.getService().getId() : null,
                favorite.getService() != null ? favorite.getService().getNome() : null,
                favorite.getService() != null && favorite.getService().getPrestador() != null
                        ? favorite.getService().getPrestador().getNome() : null,
                favorite.getCriadoEm()
        );
    }

    // Converte FavoriteRequestDTO para FavoriteModel
    public FavoriteModel toModel(FavoriteRequestDTO dto) {
        return FavoriteModel.builder()
                .build();
        // usuario e service são setados no Service Layer
    }
}
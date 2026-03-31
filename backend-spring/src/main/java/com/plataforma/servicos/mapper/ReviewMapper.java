package com.plataforma.servicos.mapper;


import com.plataforma.servicos.dto.reviewDTOS.ReviewRequestDTO;
import com.plataforma.servicos.dto.reviewDTOS.ReviewResponseDTO;
import com.plataforma.servicos.entity.ReviewModel;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    // Converte ReviewModel para ReviewResponseDTO
    public ReviewResponseDTO toResponseDTO(ReviewModel review) {
        return new ReviewResponseDTO(
                review.getId(),
                review.getService() != null ? review.getService().getId() : null,
                review.getServiceOrder() != null ? review.getServiceOrder().getId() : null,
                review.getUsuario() != null ? review.getUsuario().getId() : null,
                review.getUsuario() != null ? review.getUsuario().getNome() : null,
                review.getClassificacao(),
                review.getComentario(),
                review.getEditado(),      // adicionado
                review.getEditadoEm(),    // adicionado
                review.getCriadoEm()
        );
    }

    // Converte ReviewRequestDTO para ReviewModel
    public ReviewModel toModel(ReviewRequestDTO dto) {
        return ReviewModel.builder()
                .classificacao(dto.rating())
                .comentario(dto.comentario())
                .build();
        // service, usuario e serviceOrder são setados no Service Layer
    }
}
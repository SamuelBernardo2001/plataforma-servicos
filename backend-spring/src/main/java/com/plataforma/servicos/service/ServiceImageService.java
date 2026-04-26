package com.plataforma.servicos.service;

import com.plataforma.servicos.dto.serviceImagesDTO.ServiceImageRequestDTO;
import com.plataforma.servicos.dto.serviceImagesDTO.ServiceImageResponseDTO;
import com.plataforma.servicos.entity.ServiceImageModel;
import com.plataforma.servicos.entity.ServiceModel;
import com.plataforma.servicos.entity.UserENUM;
import com.plataforma.servicos.entity.UserModel;
import com.plataforma.servicos.mapper.ServiceImageMapper;
import com.plataforma.servicos.repository.ServiceImageRepository;
import com.plataforma.servicos.repository.ServiceRepository;
import com.plataforma.servicos.repository.UserRepository;
import com.plataforma.servicos.util.PaginatedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importe do Spring para melhor integração

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // Mantém a sessão ativa para consultas e mappers
public class ServiceImageService {

    private final ServiceImageRepository serviceImageRepository;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final ServiceImageMapper serviceImageMapper;

    // Limite máximo de imagens por serviço
    // 10 imagens são suficientes para mostrar o trabalho
    // profissionalmente sem sobrecarregar o sistema
    private static final int MAX_IMAGENS_POR_SERVICO = 10;

    // Regra: qualquer pessoa pode ver as imagens — são públicas
    // Regra: serviço deve existir
    // Usado no frontend para exibir galeria de imagens do serviço
    // Lista todas as imagens de um serviço com paginação
    // Mesmo com limite de 10 imagens mantemos paginação
    // para consistência com o padrão do sistema
    public PaginatedResponse<ServiceImageResponseDTO> findByService(
            UUID serviceId, Pageable pageable) {
        serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        Page<ServiceImageResponseDTO> page = serviceImageRepository
                .findByServiceId(serviceId, pageable)
                .map(serviceImageMapper::toResponseDTO);
        return PaginatedResponse.of(page);
    }

    // Adiciona imagem ao serviço
    // Regra: apenas o próprio PRESTADOR dono do serviço pode adicionar
    // Regra: serviço deve estar ativo
    // Regra: limite máximo de 10 imagens por serviço
    //        evita abuso e sobrecarga no sistema
    // Regra: URL deve ser válida e não vazia
    // No M7 o prestadorId virá do token JWT automaticamente
    // No M7 será integrado com Cloudinary para upload real
    // Por enquanto recebe a URL diretamente
    @Transactional // Permite a persistência e ativa o Spring Auditing
    public ServiceImageResponseDTO addImage(
            UUID serviceId, UUID prestadorId, ServiceImageRequestDTO dto) {

        ServiceModel service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        UserModel prestador = userRepository.findById(prestadorId)
                .orElseThrow(() -> new RuntimeException("Prestador não encontrado"));

        // Apenas o dono do serviço pode adicionar imagens
        if (!service.getPrestador().getId().equals(prestadorId)) {
            throw new RuntimeException(
                    "Você não tem permissão para adicionar imagens neste serviço"
            );
        }

        // Apenas PRESTADOR pode adicionar imagens
        if (!UserENUM.PRESTADOR.equals(prestador.getPerfil())) {
            throw new RuntimeException(
                    "Apenas prestadores podem adicionar imagens ao serviço"
            );
        }

        // Serviço desativado não pode receber novas imagens
        if (Boolean.FALSE.equals(service.getAtivo())) {
            throw new RuntimeException(
                    "Não é possível adicionar imagens em serviço desativado"
            );
        }

        // Verifica limite de imagens por serviço
        long totalImagens = serviceImageRepository.countByServiceId(serviceId);
        if (totalImagens >= MAX_IMAGENS_POR_SERVICO) {
            throw new RuntimeException(
                    "Limite máximo de " + MAX_IMAGENS_POR_SERVICO +
                            " imagens por serviço atingido"
            );
        }

        ServiceImageModel image = serviceImageMapper.toModel(dto);
        image.setService(service);

        return serviceImageMapper.toResponseDTO(serviceImageRepository.save(image));
    }

    // Remove imagem do serviço
    // Regra: apenas o próprio PRESTADOR dono do serviço pode remover
    // Regra: imagem deve existir
    // No M7 o prestadorId virá do token JWT automaticamente
    // No M7 será integrado com Cloudinary para deletar do storage
    @Transactional
    public void removeImage(UUID imageId, UUID prestadorId) {
        ServiceImageModel image = serviceImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Imagem não encontrada"));

        // Apenas o dono do serviço pode remover imagens
        if (!image.getService().getPrestador().getId().equals(prestadorId)) {
            throw new RuntimeException(
                    "Você não tem permissão para remover esta imagem"
            );
        }

        serviceImageRepository.delete(image);
    }
}
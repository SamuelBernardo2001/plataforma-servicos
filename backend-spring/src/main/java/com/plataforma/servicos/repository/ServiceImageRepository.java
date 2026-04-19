package com.plataforma.servicos.repository;

import com.plataforma.servicos.entity.ServiceImageModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ServiceImageRepository extends JpaRepository<ServiceImageModel, UUID> {

    // Busca imagens do serviço com paginação
    // Mantém consistência com o padrão do sistema
    Page<ServiceImageModel> findByServiceId(UUID serviceId, Pageable pageable);

    // Conta quantas imagens um serviço tem
    // Usado para limitar quantidade de imagens por serviço
    long countByServiceId(UUID serviceId);

}

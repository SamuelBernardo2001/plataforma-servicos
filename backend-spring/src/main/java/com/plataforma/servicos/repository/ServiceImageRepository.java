package com.plataforma.servicos.repository;

import com.plataforma.servicos.entity.ServiceImageModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ServiceImageRepository extends JpaRepository<ServiceImageModel, UUID> {

    // Busca todas as imagens de um serviço
    // Usado na listagem de imagens do serviço
    List<ServiceImageModel> findByServiceId(UUID serviceId);

    // Conta quantas imagens um serviço tem
    // Usado para limitar quantidade de imagens por serviço
    long countByServiceId(UUID serviceId);

}

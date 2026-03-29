package com.plataforma.servicos.repository;

import com.plataforma.servicos.entity.FavoriteModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FavoriteRepository extends JpaRepository<FavoriteModel, UUID> {
}

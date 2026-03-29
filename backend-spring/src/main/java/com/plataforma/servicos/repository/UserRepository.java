package com.plataforma.servicos.repository;

import com.plataforma.servicos.entity.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, UUID> {
}

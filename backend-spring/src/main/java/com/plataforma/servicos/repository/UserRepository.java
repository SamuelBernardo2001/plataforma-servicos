package com.plataforma.servicos.repository;

import com.plataforma.servicos.entity.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, Long> {
}

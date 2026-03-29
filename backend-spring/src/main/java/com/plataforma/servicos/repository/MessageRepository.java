package com.plataforma.servicos.repository;

import com.plataforma.servicos.entity.MessageModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MessageRepository extends JpaRepository<MessageModel, UUID> {
}

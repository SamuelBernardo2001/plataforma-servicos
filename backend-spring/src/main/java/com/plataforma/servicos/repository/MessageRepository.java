package com.plataforma.servicos.repository;

import com.plataforma.servicos.entity.MessageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<MessageModel, UUID> {

    // Busca mensagens de uma ordem ordenadas por data de envio (mais antigas primeiro)
    // Paginadas para não carregar todo o histórico de uma vez — igual ao WhatsApp
    // que carrega mensagens aos poucos quando você sobe o scroll
    Page<MessageModel> findByServiceOrderIdOrderByEnviadoEmAsc(UUID serviceOrderId, Pageable pageable);
}
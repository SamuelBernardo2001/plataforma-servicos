CREATE TABLE IF NOT EXISTS messages (
    id BINARY(16) NOT NULL,
    service_order_id BINARY(16) NOT NULL,
    remetente_id BINARY(16) NOT NULL,
    receptor_id BINARY(16) NOT NULL,
    conteudo TEXT NOT NULL,
    ler BOOLEAN NOT NULL DEFAULT FALSE,
    editado BOOLEAN NOT NULL DEFAULT FALSE,
    editado_em TIMESTAMP NULL,
    enviado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),

    -- FK para a ordem de serviço — mensagem sempre vinculada a uma ordem
    CONSTRAINT fk_message_service_order
        FOREIGN KEY (service_order_id)
        REFERENCES service_orders(id)
        ON DELETE CASCADE,

    -- FK para quem enviou
    CONSTRAINT fk_message_remetente
        FOREIGN KEY (remetente_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    -- FK para quem recebe
    CONSTRAINT fk_message_receptor
        FOREIGN KEY (receptor_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

-- Index para busca por ordem — usado no findByServiceOrderId()
-- Essencial para o chat carregar mensagens da ordem rapidamente
CREATE INDEX idx_messages_service_order ON messages(service_order_id);

-- Index para busca por receptor e leitura
-- Usado no marcarComoLida() para encontrar mensagens não lidas
CREATE INDEX idx_messages_receptor_ler ON messages(receptor_id, ler);

-- Index para ordenação por data de envio
-- Usado no findByServiceOrderIdOrderByEnviadoEmAsc()
CREATE INDEX idx_messages_enviado_em ON messages(enviado_em);
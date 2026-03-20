CREATE TABLE IF NOT EXISTS reviews (
    id BINARY(16) PRIMARY KEY,
    service_id BINARY(16),
    user_id BINARY(16),
    classificacao INT,
    comentario TEXT,
    criado_em TIMESTAMP
);
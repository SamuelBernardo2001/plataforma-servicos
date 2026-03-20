CREATE TABLE IF NOT EXISTS favorites (
    id BINARY(16) PRIMARY KEY,
    user_id BINARY(16),
    service_id BINARY(16),
    criado_em TIMESTAMP
);
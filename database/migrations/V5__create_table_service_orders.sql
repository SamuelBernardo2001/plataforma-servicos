CREATE TABLE IF NOT EXISTS service_orders (
    id BINARY(16) PRIMARY KEY,
    service_id BINARY(16),
    client_id BINARY(16),
    provedor_id BINARY(16),
    price DECIMAL(10,2),
    status VARCHAR(50),
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    concluido_em TIMESTAMP
);
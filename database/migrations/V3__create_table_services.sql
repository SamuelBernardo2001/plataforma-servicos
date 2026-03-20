CREATE TABLE IF NOT EXISTS services (
    id BINARY(16) PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    preco DECIMAL(10,2),
    prestador_id BINARY(16),
    categoria_id BINARY(16),
    ativo BOOLEAN DEFAULT TRUE,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP
);
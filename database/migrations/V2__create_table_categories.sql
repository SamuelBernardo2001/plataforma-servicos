CREATE TABLE IF NOT EXISTS categories (
    id BINARY(16) PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    ativo BOOLEAN DEFAULT TRUE,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP
);
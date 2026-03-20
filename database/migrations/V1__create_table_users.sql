CREATE TABLE IF NOT EXISTS users (
    id BINARY(16) PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    perfil VARCHAR(50),
    telefone VARCHAR(20),
    ativo BOOLEAN DEFAULT TRUE,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP
);
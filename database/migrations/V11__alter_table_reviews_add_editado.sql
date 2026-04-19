-- Adiciona campo editado — indica se avaliação foi modificada após criação
-- Valor padrão FALSE — avaliação começa como não editada
ALTER TABLE reviews
    ADD COLUMN editado BOOLEAN NOT NULL DEFAULT FALSE;

-- Adiciona campo editado_em — registra quando foi a última edição
-- NULL se nunca foi editada
ALTER TABLE reviews
    ADD COLUMN editado_em TIMESTAMP NULL;
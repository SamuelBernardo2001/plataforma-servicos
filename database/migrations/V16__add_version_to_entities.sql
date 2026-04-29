-- Adiciona campo version para Optimistic Locking
-- @Version do Hibernate usa esse campo para detectar
-- conflitos de atualizacao simultanea

-- users — dados do perfil podem ser editados simultaneamente
ALTER TABLE users
    ADD COLUMN version BIGINT NOT NULL DEFAULT 0;

-- services — prestador pode editar enquanto cliente visualiza
ALTER TABLE services
    ADD COLUMN version BIGINT NOT NULL DEFAULT 0;

-- service_orders — entidade mais critica
-- cliente e prestador podem tentar mudar status ao mesmo tempo
ALTER TABLE service_orders
    ADD COLUMN version BIGINT NOT NULL DEFAULT 0;
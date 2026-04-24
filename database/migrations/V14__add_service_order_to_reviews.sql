ALTER TABLE reviews ADD COLUMN service_order_id BINARY(16);

-- Opcional: Adicionar a constraint para integridade
ALTER TABLE reviews ADD CONSTRAINT fk_reviews_service_order
FOREIGN KEY (service_order_id) REFERENCES service_orders(id);
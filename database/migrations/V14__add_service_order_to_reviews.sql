ALTER TABLE reviews
ADD COLUMN service_order_id BINARY(16) UNIQUE,
ADD CONSTRAINT fk_reviews_service_order FOREIGN KEY (service_order_id) REFERENCES service_orders(id);
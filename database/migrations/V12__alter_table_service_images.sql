-- Adiciona NOT NULL e FK que faltavam na V4
-- url TEXT → VARCHAR(2048) mais adequado para URLs
ALTER TABLE service_images
    MODIFY COLUMN service_id BINARY(16) NOT NULL;

ALTER TABLE service_images
    MODIFY COLUMN url VARCHAR(2048) NOT NULL;

ALTER TABLE service_images
    ADD CONSTRAINT fk_service_image_service
        FOREIGN KEY (service_id)
        REFERENCES services(id)
        ON DELETE CASCADE;

-- Index para busca de imagens por serviço
-- Usado no findByServiceId() do repository
CREATE INDEX idx_service_images_service_id
    ON service_images(service_id);
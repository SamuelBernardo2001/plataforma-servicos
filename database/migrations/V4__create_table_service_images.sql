CREATE TABLE IF NOT EXISTS service_images (
    id BINARY(16) PRIMARY KEY,
    service_id BINARY(16),
    url TEXT
);
CREATE TABLE IF NOT EXISTS reports (
    id BINARY(16) NOT NULL,
    reporter_id BINARY(16) NOT NULL,
    reported_user_id BINARY(16) NOT NULL,
    service_order_id BINARY(16) NULL,
    razao VARCHAR(255) NOT NULL,
    descricao VARCHAR(1000),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDENTE',
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),

    -- FK para quem denunciou
    CONSTRAINT fk_report_reporter
        FOREIGN KEY (reporter_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    -- FK para quem foi denunciado
    CONSTRAINT fk_report_reported_user
        FOREIGN KEY (reported_user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    -- FK para ordem relacionada (opcional)
    CONSTRAINT fk_report_service_order
        FOREIGN KEY (service_order_id)
        REFERENCES service_orders(id)
        ON DELETE SET NULL,

    -- Apenas valores válidos do ReportStatusEnum
    CONSTRAINT chk_report_status
        CHECK (status IN ('PENDENTE', 'RESOLVIDA', 'REJEITADA'))
);

-- Index para busca por status — usado no findByStatus() do ADMIN
CREATE INDEX idx_reports_status ON reports(status);

-- Index para busca por quem foi denunciado
CREATE INDEX idx_reports_reported_user ON reports(reported_user_id);

-- Index para busca por quem denunciou
CREATE INDEX idx_reports_reporter ON reports(reporter_id);
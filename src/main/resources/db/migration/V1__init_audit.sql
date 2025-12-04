CREATE TABLE audit_logs (
                            id UUID PRIMARY KEY,
                            user_id VARCHAR(255) NOT NULL,
                            action VARCHAR(255) NOT NULL,
                            timestamp TIMESTAMP NOT NULL,
                            details TEXT
);
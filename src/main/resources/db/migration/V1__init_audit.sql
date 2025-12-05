CREATE TABLE audit_logs (
                            id UUID PRIMARY KEY,
                            username VARCHAR(255) NOT NULL,
                            action VARCHAR(255) NOT NULL,
                            resource VARCHAR(255),
                            success BOOLEAN,
                            timestamp TIMESTAMP NOT NULL,
                            details VARCHAR(1024)
);
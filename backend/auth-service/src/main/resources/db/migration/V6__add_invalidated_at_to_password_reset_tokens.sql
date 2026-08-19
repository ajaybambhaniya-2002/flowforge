ALTER TABLE password_reset_tokens
    ADD COLUMN invalidated_at TIMESTAMP NULL;
CREATE TABLE email_verification_tokens (
   id BIGSERIAL PRIMARY KEY,

   token VARCHAR(255) NOT NULL UNIQUE,

   user_id BIGINT NOT NULL,

   created_at TIMESTAMP NOT NULL,

   expires_at TIMESTAMP NOT NULL,

   verified_at TIMESTAMP NULL,

   CONSTRAINT fk_email_verification_token_user
       FOREIGN KEY (user_id)
           REFERENCES users(id)
);
CREATE INDEX idx_email_verification_tokens_user_id
    ON email_verification_tokens(user_id);
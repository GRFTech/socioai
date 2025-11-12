CREATE TABLE user_tb
(
    id       BINARY(16)   NOT NULL,
    username VARCHAR(254) NULL,
    password VARCHAR(255) NOT NULL,
    role_id  BIGINT       NULL,
    CONSTRAINT pk_user_tb PRIMARY KEY (id)
);

ALTER TABLE user_tb
    ADD CONSTRAINT uc_user_tb_username UNIQUE (username);

CREATE INDEX idx_user_email ON user_tb (username);

ALTER TABLE user_tb
    ADD CONSTRAINT FK_USER_TB_ON_ROLE FOREIGN KEY (role_id) REFERENCES roles_tb (id);
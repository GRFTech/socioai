CREATE TABLE categoria_tb
(
    id      BIGINT AUTO_INCREMENT NOT NULL,
    nome    VARCHAR(45)           NOT NULL,
    tipo    VARCHAR(10)           NOT NULL,
    user_id BINARY(16)            NULL,
    CONSTRAINT pk_categoria_tb PRIMARY KEY (id)
);

ALTER TABLE categoria_tb
    ADD CONSTRAINT FK_CATEGORIA_TB_ON_USER FOREIGN KEY (user_id) REFERENCES user_tb (id);
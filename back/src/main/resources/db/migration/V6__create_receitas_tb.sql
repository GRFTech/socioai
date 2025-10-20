CREATE TABLE receitas_tb
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    descricao    VARCHAR(100)          NOT NULL,
    valor        DOUBLE                NOT NULL,
    data_criacao datetime              NOT NULL,
    categoria_id BIGINT                NULL,
    CONSTRAINT pk_receitas_tb PRIMARY KEY (id)
);

ALTER TABLE receitas_tb
    ADD CONSTRAINT FK_RECEITAS_TB_ON_CATEGORIA FOREIGN KEY (categoria_id) REFERENCES categoria_tb (id);
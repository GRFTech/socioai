CREATE TABLE despesas_tb
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    descricao    VARCHAR(100)          NOT NULL,
    valor        DOUBLE                NOT NULL,
    data_criacao datetime              NOT NULL,
    categoria_id BIGINT                NULL,
    CONSTRAINT pk_despesas_tb PRIMARY KEY (id)
);

ALTER TABLE despesas_tb
    ADD CONSTRAINT FK_DESPESAS_TB_ON_CATEGORIA FOREIGN KEY (categoria_id) REFERENCES categoria_tb (id);
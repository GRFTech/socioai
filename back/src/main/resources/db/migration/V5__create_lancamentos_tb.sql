CREATE TABLE lancamentos_tb
(
    id              BIGINT AUTO_INCREMENT NOT NULL,
    descricao       VARCHAR(100)          NOT NULL,
    valor           DOUBLE                NOT NULL,
    tipo_lancamento SMALLINT              NOT NULL,
    data_criacao    datetime              NOT NULL,
    meta_id         BIGINT                NULL,
    CONSTRAINT pk_lancamentos_tb PRIMARY KEY (id)
);

ALTER TABLE lancamentos_tb
    ADD CONSTRAINT FK_LANCAMENTOS_TB_ON_META FOREIGN KEY (meta_id) REFERENCES metas_tb (id);
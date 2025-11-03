CREATE TABLE metas_tb
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    descricao   VARCHAR(45)           NOT NULL,
    valor_atual DOUBLE                NOT NULL,
    data_inicio date                  NOT NULL,
    data_fim    date                  NOT NULL,
    CONSTRAINT pk_metas_tb PRIMARY KEY (id)
);
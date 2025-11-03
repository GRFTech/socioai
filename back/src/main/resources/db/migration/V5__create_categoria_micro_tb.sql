CREATE TABLE categoria_micro_tb
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    meta_id      BIGINT                NULL,
    categoria_id BIGINT                NULL,
    CONSTRAINT pk_categoria_micro_tb PRIMARY KEY (id)
);

ALTER TABLE categoria_micro_tb
    ADD CONSTRAINT FK_CATEGORIA_MICRO_TB_ON_CATEGORIA FOREIGN KEY (categoria_id) REFERENCES categoria_tb (id);

ALTER TABLE categoria_micro_tb
    ADD CONSTRAINT FK_CATEGORIA_MICRO_TB_ON_META FOREIGN KEY (meta_id) REFERENCES metas_tb (id);
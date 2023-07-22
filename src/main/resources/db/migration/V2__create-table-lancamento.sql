CREATE TABLE financas.lancamento
(
    id bigserial NOT NULL PRIMARY KEY ,
    descricao character varying(100) NOT NULL,
    mes integer NOT NULL,
    ano integer NOT NULL,
    valor numeric(16,2) NOT NULL,
    tipo character varying(20) NOT NULL,
    status character varying(20) NOT NULL,
    id_usuario bigint REFERENCES financas.usuario (id) NOT NULL,
    data_cadastro date default now()
);
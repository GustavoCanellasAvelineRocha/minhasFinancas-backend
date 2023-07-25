CREATE TABLE financas.usuario
(
    id bigserial PRIMARY KEY,
    nome character varying(150),
    email character varying(100),
    senha character varying(20),
    data_cadastro date DEFAULT CURRENT_TIMESTAMP
);
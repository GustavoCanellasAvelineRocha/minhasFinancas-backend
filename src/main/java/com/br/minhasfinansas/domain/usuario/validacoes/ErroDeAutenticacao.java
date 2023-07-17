package com.br.minhasfinansas.domain.usuario.validacoes;

public class ErroDeAutenticacao extends RuntimeException{
    public ErroDeAutenticacao(String e) {
        super(e);
    }
}

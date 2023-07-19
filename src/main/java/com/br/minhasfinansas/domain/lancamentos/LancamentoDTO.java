package com.br.minhasfinansas.domain.lancamentos;

public record LancamentoDTO(Long id, String descricao, Integer mes, Integer ano, Double valor, Long idUsuario, String tipo, String status) {
    public LancamentoDTO(Lancamento lancamento) {
        this(lancamento.getId(), lancamento.getDescricao(), lancamento.getMes(), lancamento.getAno(), lancamento.getValor(), lancamento.getUsuario().getId(),lancamento.getTipo().toString(),lancamento.getStatus().toString());
    }
}

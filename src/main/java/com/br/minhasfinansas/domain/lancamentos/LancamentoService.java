package com.br.minhasfinansas.domain.lancamentos;

import java.util.List;

public interface LancamentoService {
    Lancamento salvar(Lancamento lancamento);
    Lancamento atualizar(Lancamento lancamento);
    void deletar(Lancamento lancamento);
    List<Lancamento> buscar(Lancamento lancamentoFiltro);
    void atualizarStatus(Lancamento lancamento, StatusLancamento statusLancamento);
    void validar(Lancamento lancamento);
    Lancamento findById(Long id);
    Double obterSaldoPorUsuario(Long id);
    List<Lancamento> findAll();
}

package com.br.minhasfinansas.domain.lancamentos;

import com.br.minhasfinansas.domain.usuario.validacoes.RegraNegocioException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

public class LancamentoServiceImp implements LancamentoService{
    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Override
    @Transactional
    public Lancamento salvar(Lancamento lancamento) {
        validar(lancamento);
        lancamento.setStatus(StatusLancamento.PEDENTE);
        return lancamentoRepository.save(lancamento);
    }

    @Override
    @Transactional
    public Lancamento atualizar(Lancamento lancamento) {
        Objects.requireNonNull(lancamento.getId());
        validar(lancamento);
        return lancamentoRepository.save(lancamento);
    }

    @Override
    @Transactional
    public void deletar(Lancamento lancamento) {
        Objects.requireNonNull(lancamento.getId());
        lancamentoRepository.delete(lancamento);
    }

    @Override
    @Transactional()
    public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
        return null;
    }

    @Override
    public void atualizarStatus(Lancamento lancamento, StatusLancamento statusLancamento) {
        lancamento.setStatus(statusLancamento);
        atualizar(lancamento);
    }

    @Override
    public void validar(Lancamento lancamento) {

        if(lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals("")){
            throw new RegraNegocioException("Informe uma descricao valida");
        }

        if(lancamento.getMes() < 1 || lancamento.getMes() > 12){
            throw new RegraNegocioException("Informe um mes valido");
        }

        if(lancamento.getAno() == 0 || (lancamento.getAno() >= 1000 && lancamento.getAno() <= 9999)){
            throw new RegraNegocioException("Informe um ano valido");
        }

        if(lancamento.getUsuario() == null || lancamento.getUsuario().getId() == null){
            throw new RegraNegocioException("Informe um usuario");
        }

        if(lancamento.getValor() < 1){
            throw new RegraNegocioException("Informe um valor valido");
        }

        if(lancamento.getTipo() == null){
            throw new RegraNegocioException("Informe um tipo de lancamento");
        }
    }

}

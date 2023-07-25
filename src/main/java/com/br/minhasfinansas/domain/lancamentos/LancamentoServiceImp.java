package com.br.minhasfinansas.domain.lancamentos;

import com.br.minhasfinansas.domain.usuario.validacoes.RegraNegocioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class LancamentoServiceImp implements LancamentoService{
    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Override
    @Transactional
    public Lancamento salvar(Lancamento lancamento) {
        validar(lancamento);
        lancamento.setStatus(StatusLancamento.PENDENTE);
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
        lancamentoRepository.deleteById(lancamento.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
        Example<Lancamento> example = Example.of(lancamentoFiltro, ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));
        return lancamentoRepository.findAll(example);
    }

    @Override
    public void atualizarStatus(Lancamento lancamento, StatusLancamento statusLancamento) {
        lancamento.setStatus(statusLancamento);
        atualizar(lancamento);
    }

    @Override
    public void validar(Lancamento lancamento) {

//        if(lancamento.getId()!= null){
//            if(lancamentoRepository.existsById(lancamento.getId())){
//                throw new RegraNegocioException("Esse id ja esta sendo utilizado");
//            }
//        }

        if(lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals("")){
            throw new RegraNegocioException("Informe uma descricao valida");
        }

        if(lancamento.getMes() < 1 || lancamento.getMes() > 12){
            throw new RegraNegocioException("Informe um mes valido");
        }

        if((lancamento.getAno()<1000 || lancamento.getAno() > 9999)){
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

    @Override
    public Lancamento findById(Long id) {
        var lancamento = lancamentoRepository.findById(id);

        if(lancamento.isPresent())return lancamento.get();
        else throw new RegraNegocioException("Lancamento nao encontrado");
    }

    @Override
    @Transactional(readOnly = true)
    public Double obterSaldoPorUsuario(Long id) {
        Double receitas = lancamentoRepository.obterSaldoPorTipoLancamentoEUsuario(id,TipoLancamento.RECEITA,StatusLancamento.EFETIVADO);
        Double despesas = lancamentoRepository.obterSaldoPorTipoLancamentoEUsuario(id,TipoLancamento.DESPESA,StatusLancamento.EFETIVADO);

        if (receitas==null){
            receitas= (double) 0;
        }

        if (despesas==null){
            despesas= (double) 0;
        }

        return receitas - despesas;
    }

    @Override
    public List<Lancamento> findAll() {
        return lancamentoRepository.findAll();
    }

}

package com.br.minhasfinansas.repository;

import com.br.minhasfinansas.domain.lancamentos.Lancamento;
import com.br.minhasfinansas.domain.lancamentos.LancamentoRepository;
import com.br.minhasfinansas.domain.lancamentos.StatusLancamento;
import com.br.minhasfinansas.domain.lancamentos.TipoLancamento;
import com.br.minhasfinansas.domain.usuario.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class LancamentoRepositoryTest {
    @Autowired
    LancamentoRepository lancamentoRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    @Transactional
    public void VerificaSeSalvouLancamento(){

        Lancamento lancamento = criaLancamento();

        lancamento = lancamentoRepository.save(lancamento);

        assertThat(lancamento.getId()).isNotNull();
    }

    @Test
    @Transactional
    public void VerificaSeDeletouLancamento(){

        Lancamento lancamento = criaLancamento();

        entityManager.persist(lancamento);
        lancamento = entityManager.find(Lancamento.class,lancamento.getId());

        lancamentoRepository.deleteById(lancamento.getId());

        Lancamento lancamentoNull = entityManager.find(Lancamento.class,lancamento.getId());
        assertThat(lancamentoNull).isNull();
    }

    @Test
    @Transactional
    public void verificaSeAtualizouLancamento(){

        Lancamento lancamento = criaLancamento();

        entityManager.persist(lancamento);

        lancamento.setAno(2024);
        lancamento.setStatus(StatusLancamento.CANCELADO);
        lancamento.setDescricao("teste de descricao");

        lancamentoRepository.save(lancamento);
        Lancamento lancamentoAtt = entityManager.find(Lancamento.class,lancamento.getId());

        assertThat(lancamentoAtt.getAno()).isEqualTo(2024);
        assertThat(lancamentoAtt.getStatus()).isEqualTo(StatusLancamento.CANCELADO);
        assertThat(lancamentoAtt.getDescricao()).isEqualTo("teste de descricao");
    }

    @Test
    @Transactional
    public void verificaSeBuscouLancamentoPorId(){

        Lancamento lancamento = criaLancamento();

        entityManager.persist(lancamento);

        var lancamentoFinded = lancamentoRepository.findById(lancamento.getId());

        assertThat(lancamentoFinded.isPresent()).isTrue();
    }

    public Lancamento criaLancamento(){
        Lancamento lancamento = Lancamento.builder()
                .usuario(criaUsuario())
                .ano(2023).mes(1)
                .descricao("lancamento qualquer")
                .valor(200D)
                .tipo(TipoLancamento.DESPESA)
                .status(StatusLancamento.PENDENTE)
                .build();
        return lancamento;
    }

    public Usuario criaUsuario(){
        Usuario usuario = Usuario.builder().nome("test").senha("123").email("test@email.com").build();
        return usuario;
    }

}

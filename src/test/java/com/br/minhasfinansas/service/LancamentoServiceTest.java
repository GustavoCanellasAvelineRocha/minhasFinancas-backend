package com.br.minhasfinansas.service;

import com.br.minhasfinansas.domain.lancamentos.*;
import com.br.minhasfinansas.domain.usuario.Usuario;
import com.br.minhasfinansas.domain.usuario.validacoes.RegraNegocioException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

@SpringBootTest
@ActiveProfiles("test")
public class LancamentoServiceTest {

    @SpyBean
    LancamentoService lancamentoService;

    @MockBean
    LancamentoRepository lancamentoRepository;

    @Test
    public void VerificaSeSalvouUmLancamento(){
        Lancamento lancamento = criaLancamento();
        Mockito.doNothing().when(lancamentoService).validar(lancamento);

        Lancamento lancamentoSalvo = criaLancamento();
        lancamentoSalvo.setId(1L);
        lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
        Mockito.when(lancamentoRepository.save(lancamento)).thenReturn(lancamentoSalvo);

        Lancamento lancamentoVerifica = lancamentoService.salvar(lancamento);

        assertThat(lancamentoVerifica.getId()).isEqualTo(lancamentoSalvo.getId());
        assertThat(lancamentoVerifica.getStatus()).isEqualTo(lancamentoSalvo.getStatus());
    }

    @Test
    public void verificaSeNaoSalvouLancamentoQuandoHouverErro(){
        Lancamento lancamento = criaLancamento();
        Mockito.doThrow(RegraNegocioException.class).when(lancamentoService).validar(lancamento);

        catchThrowableOfType(()-> lancamentoService.salvar(lancamento),RegraNegocioException.class);

        Mockito.verify(lancamentoRepository,Mockito.never()).save(lancamento);
    }

    @Test
    public void verificaSeAtualizarUmLancamento(){
        Lancamento lancamentoSalvo = criaLancamento();
        lancamentoSalvo.setId(1L);
        lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
        Mockito.doNothing().when(lancamentoService).validar(lancamentoSalvo);
        Mockito.when(lancamentoRepository.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);

        lancamentoService.atualizar(lancamentoSalvo);

        Mockito.verify(lancamentoRepository,Mockito.times(1)).save(lancamentoSalvo);
    }

    @Test
    public void verificaSeHouveErroAoAtualizarUmLancamento(){
        Lancamento lancamento = criaLancamento();

        catchThrowableOfType(()->lancamentoService.atualizar(lancamento),NullPointerException.class);
        Mockito.verify(lancamentoRepository,Mockito.never()).save(lancamento);
    }

    @Test
    public void verificaSeDeletouUmLancamento(){
        Lancamento lancamento = criaLancamento();
        lancamento.setId(1L);

        lancamentoService.deletar(lancamento);

        Mockito.verify(lancamentoRepository).deleteById(lancamento.getId() );
    }

    @Test
    public void verificaSeOcorreuErroAoDeletarUmLancamento(){
        Lancamento lancamento = criaLancamento();

        catchThrowableOfType(()->lancamentoService.deletar(lancamento),NullPointerException.class);
        Mockito.verify(lancamentoRepository,Mockito.never()).deleteById(lancamento.getId());
    }

    @Test
    public void verificaSeBuscouLancamento(){
        Lancamento lancamento = criaLancamento();
        lancamento.setId(1L);

        List<Lancamento> lancamentos = List.of(lancamento);
        Mockito.when(lancamentoRepository.findAll(Mockito.any(Example.class))).thenReturn(lancamentos);

        List<Lancamento> resultado = lancamentoService.buscar(lancamento);

        assertThat(resultado).isNotEmpty().hasSize(1).contains(lancamento);
    }

    @Test
    public void verificaAtualizouStatus(){
        Lancamento lancamento = criaLancamento();
        lancamento.setId(1L);
        lancamento.setStatus(StatusLancamento.PENDENTE);
        Mockito.doReturn(lancamento).when(lancamentoService).atualizar(lancamento);

        lancamentoService.atualizarStatus(lancamento,StatusLancamento.EFETIVADO);

        assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.EFETIVADO);
        Mockito.verify(lancamentoService).atualizar(lancamento);
    }

    @Test
    public void verificaSeObtevePorId(){
        Lancamento lancamento = criaLancamento();
        lancamento.setId(1L);

        Mockito.when(lancamentoRepository.findById(lancamento.getId())).thenReturn(Optional.of(lancamento));

        var resultado = lancamentoService.findById(lancamento.getId());

        assertThat(resultado).isEqualTo(lancamento);
    }

    @Test
    public void verificaSeOcorreuErroAoBuscarPorId(){
        Lancamento lancamento = criaLancamento();
        lancamento.setId(1L);

        Mockito.when(lancamentoRepository.findById(2L)).thenReturn(Optional.empty());

        var resultado  = catchThrowableOfType(()->lancamentoService.findById(lancamento.getId()),RegraNegocioException.class);
        assertThat(resultado.getClass()).isEqualTo(RegraNegocioException.class);
    }

    @Test
    public void verificaSeValidouCorretamente(){
        Lancamento lancamento = criaLancamento();

        lancamentoService.validar(lancamento);

        Mockito.verify(lancamentoService).validar(lancamento);
    }

    @Test
    public void verificaSeValidouAlgoIncorreto(){
        Lancamento lancamento = criaLancamento();
        //validando descricao
        lancamento.setDescricao(null);
        var result =catchThrowableOfType(()->lancamentoService.validar(lancamento),RegraNegocioException.class);
        assertThat(result.getMessage()).isEqualTo("Informe uma descricao valida");
        lancamento.setDescricao("lancamento qualquer");

        //validando mes
        lancamento.setMes(0);
        result = catchThrowableOfType(()->lancamentoService.validar(lancamento),RegraNegocioException.class);
        assertThat(result.getMessage()).isEqualTo("Informe um mes valido");

        lancamento.setMes(13);
        result = catchThrowableOfType(()->lancamentoService.validar(lancamento),RegraNegocioException.class);
        assertThat(result.getMessage()).isEqualTo("Informe um mes valido");

        lancamento.setMes(1);

        //validando ano
        lancamento.setAno(999);
        result = catchThrowableOfType(()->lancamentoService.validar(lancamento),RegraNegocioException.class);
        assertThat(result.getMessage()).isEqualTo("Informe um ano valido");

        lancamento.setAno(10000);
        result = catchThrowableOfType(()->lancamentoService.validar(lancamento),RegraNegocioException.class);
        assertThat(result.getMessage()).isEqualTo("Informe um ano valido");

        lancamento.setAno(2023);

        //validando usuario
        lancamento.getUsuario().setId(null);
        result = catchThrowableOfType(()->lancamentoService.validar(lancamento),RegraNegocioException.class);
        assertThat(result.getMessage()).isEqualTo("Informe um usuario");

        lancamento.setUsuario(null);
        result = catchThrowableOfType(()->lancamentoService.validar(lancamento),RegraNegocioException.class);
        assertThat(result.getMessage()).isEqualTo("Informe um usuario");

        lancamento.setUsuario(criaUsuario());

        //validando valor
        lancamento.setValor(0D);
        result = catchThrowableOfType(()->lancamentoService.validar(lancamento),RegraNegocioException.class);
        assertThat(result.getMessage()).isEqualTo("Informe um valor valido");

        lancamento.setValor(200D);

        //validando valor
        lancamento.setTipo(null);
        result = catchThrowableOfType(()->lancamentoService.validar(lancamento),RegraNegocioException.class);
        assertThat(result.getMessage()).isEqualTo("Informe um tipo de lancamento");
    }

    @Test
    @Transactional(readOnly = true)
    public void verificaSeObteveOSaldoCorreto(){
        Lancamento lancamento = criaLancamento();

        Mockito.when(lancamentoRepository.obterSaldoPorTipoLancamentoEUsuario(1L,TipoLancamento.RECEITA,StatusLancamento.EFETIVADO))
                .thenReturn(400D);
        Mockito.when(lancamentoRepository.obterSaldoPorTipoLancamentoEUsuario(1L,TipoLancamento.DESPESA,StatusLancamento.EFETIVADO))
                .thenReturn(200D);

        assertThat(lancamentoService.obterSaldoPorUsuario(1L)).isEqualTo(200D);
    }

    public Lancamento criaLancamento(){
        Lancamento lancamento = Lancamento.builder()
                .usuario(criaUsuario())
                .ano(2023)
                .mes(1)
                .descricao("lancamento qualquer")
                .valor(200D)
                .tipo(TipoLancamento.DESPESA)
                .status(StatusLancamento.PENDENTE)
                .build();
        return lancamento;
    }

    public Usuario criaUsuario(){
        Usuario usuario = Usuario.builder().id(1L).nome("test").senha("123").email("test@email.com").build();
        return usuario;
    }

}

package com.br.minhasfinansas.service;

import com.br.minhasfinansas.domain.usuario.Usuario;
import com.br.minhasfinansas.domain.usuario.UsuarioRepository;
import com.br.minhasfinansas.domain.usuario.UsuarioServiceImp;
import com.br.minhasfinansas.domain.usuario.validacoes.ErroDeAutenticacao;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
public class UsuarioServiceTest {

    @SpyBean
    UsuarioServiceImp usuarioService;
    @MockBean
    UsuarioRepository usuarioRepository;

    @Test
    public void verificaSeSalvouUsuario(){
        Mockito.doNothing().when(usuarioService).validarEmail(Mockito.anyString());
        Usuario usuario = Usuario.builder().id(1L).nome("Usuario").email("usuario@email.com").senha("senha").build();
        Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class))).thenReturn(usuario);


    }

    @Test
    public void verificaSeLancaExceptionAoRepetirUsuario(){
        Usuario usuario = Usuario.builder().nome("Usuario").email("usuario@email.com").senha("senha").build();
        Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
        Mockito.when(usuarioService.salvarUsuario(usuario)).thenThrow(ErroDeAutenticacao.class);
    }

    @Test
    public void VerificaSeEmailEstaValidoParaSalvar(){
        Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
        Assertions.assertThatCode(()->usuarioService.validarEmail("usuario@email.com")).doesNotThrowAnyException();
    }

    @Test
    public void VerificaSeLancaExceptionSeJaExisteEmail(){
        Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(true);
        Assertions.assertThatThrownBy(()->usuarioService.validarEmail("usuario@email.com"));
    }

    @Test
    public void VerificaSeEstaAutenticado(){
            Usuario usuario = Usuario.builder().nome("Usuario").email("usuario@email.com").senha("senha").build();

            Mockito.when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));

            Assertions.assertThatCode(()->usuarioService.autenticar(usuario.getEmail(), usuario.getSenha())).doesNotThrowAnyException();
            Usuario result = usuarioService.autenticar(usuario.getEmail(), usuario.getSenha());

            Assertions.assertThat(result).isNotNull();
    }

    @Test
    public void VerificaSeNaoEstaAutenticadoPorNaoAcharUsuario(){
            Usuario usuario = Usuario.builder().nome("Usuario").email("usuario@email.com").senha("senha").build();

            Mockito.when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.empty());

            Assertions.assertThatThrownBy(()->usuarioService.autenticar(usuario.getEmail(), usuario.getSenha()));
    }

    @Test
    public void VerificaSeNaoEstaAutenticadoPorNaoSerSenhaCerta(){
            Usuario usuario = Usuario.builder().nome("Usuario").email("usuario@email.com").senha("senha").build();

            Mockito.when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));

            Throwable throwable = Assertions.catchThrowable(()->usuarioService.autenticar(usuario.getEmail(), "senhaErrada"));
            Assertions.assertThat(throwable).isInstanceOf(ErroDeAutenticacao.class).hasMessage("Senha incorreta");

    }
}

package com.br.minhasfinansas.repository;

import com.br.minhasfinansas.domain.usuario.Usuario;
import com.br.minhasfinansas.domain.usuario.UsuarioRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class UsuarioRepositoryTest {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    public void VerificaExistenciaDeUmEmail(){
        Usuario usuario = Usuario.builder().nome("Usuario").email("usuario@email.com").senha("senha").build();
        em.persist(usuario);

        boolean result = usuarioRepository.existsByEmail(usuario.getEmail());

        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void VerificaNaoHaExistenciaDeUmEmail(){
        boolean result = usuarioRepository.existsByEmail("usuario@email.com");

        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void VerificaSeExisteUsuarioPeloEmail(){
        Usuario usuario = Usuario.builder().nome("Usuario").email("usuario@email.com").senha("senha").build();
        em.persist(usuario);
        Optional<Usuario> usuarioFind = usuarioRepository.findByEmail(usuario.getEmail());

        Assertions.assertThat(usuario).isEqualTo(usuarioFind.get());
    }

    @Test
    public void VerificaSeNaoExisteUsuarioPeloEmail(){
        Optional<Usuario> usuarioFind = usuarioRepository.findByEmail("usuario@email.com");

        Assertions.assertThat(usuarioFind.isPresent()).isFalse();
    }

}

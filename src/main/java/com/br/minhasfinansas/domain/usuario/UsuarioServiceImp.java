package com.br.minhasfinansas.domain.usuario;

import com.br.minhasfinansas.domain.usuario.validacoes.ErroDeAutenticacao;
import com.br.minhasfinansas.domain.usuario.validacoes.RegraNegocioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@EnableAsync
public class UsuarioServiceImp implements UsuarioService{

    @Autowired
    private UsuarioRepository usuarioRepository;

    public UsuarioServiceImp(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario autenticar(String email, String senha) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

        if(usuario.isEmpty()){
            throw new ErroDeAutenticacao("Usuario nao encontrado");
        }

        if(!usuario.get().getSenha().equals(senha)){
            throw new ErroDeAutenticacao("Senha incorreta");
        }

        return usuario.get();
    }

    @Override
    @Transactional
    public Usuario salvarUsuario(Usuario usuario) {
        validarEmail(usuario.getEmail());
        return usuarioRepository.save(usuario);
    }

    @Override
    public void validarEmail(String email) {
        if(email == null || email.equals("")) throw new RegraNegocioException("Digite um email compativel");

        boolean existe = usuarioRepository.existsByEmail(email);
        if (existe) throw new RegraNegocioException("ja existe um usuario cadastrado com este email.");
    }

    @Override
    public Usuario findById(Long idUsuario) {
        var usuario = usuarioRepository.findById(idUsuario);

        if (usuario.isPresent())return usuario.get();
        else throw new RegraNegocioException("Usuario nao encontrado");
    }

    @Override
    public boolean existsById(Long idUsuario) {
        return usuarioRepository.existsById(idUsuario);
    }

    @Override
    @Transactional
    public Usuario salvarConvidado(Usuario usuario) {
        Usuario convidado = usuarioRepository.save(usuario);
        agendarExclusaoConvidado(convidado.getId(),1800000);

        return convidado;
    }

    @Async
    public void agendarExclusaoConvidado(Long usuarioId, long delay) {
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(delay);
                usuarioRepository.deleteById(usuarioId);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        });
    }
}

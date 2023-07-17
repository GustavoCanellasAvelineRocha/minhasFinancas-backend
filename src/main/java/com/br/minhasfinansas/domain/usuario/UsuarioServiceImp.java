package com.br.minhasfinansas.domain.usuario;

import com.br.minhasfinansas.domain.usuario.validacoes.ErroDeAutenticacao;
import com.br.minhasfinansas.domain.usuario.validacoes.RegraNegocioException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
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
        boolean existe = usuarioRepository.existsByEmail(email);
        if (existe) throw new RegraNegocioException("ja existe um usuario cadastrado com este email.");
    }
}

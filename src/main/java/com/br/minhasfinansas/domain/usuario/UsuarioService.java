package com.br.minhasfinansas.domain.usuario;

public interface UsuarioService {
    Usuario autenticar(String email,String senha);
    Usuario salvarUsuario(Usuario usuario);
    void validarEmail(String email);
}

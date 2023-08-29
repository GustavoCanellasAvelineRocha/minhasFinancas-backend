package com.br.minhasfinansas.domain.usuario;

public interface UsuarioService {
    Usuario autenticar(String email,String senha);
    Usuario salvarUsuario(Usuario usuario);
    void validarEmail(String email);
    Usuario findById(Long idUsuario);
    boolean existsById(Long idUsuario);
    Usuario salvarConvidado(Usuario usuario);
    void agendarExclusaoConvidado(Long usuarioId,long delay);
}

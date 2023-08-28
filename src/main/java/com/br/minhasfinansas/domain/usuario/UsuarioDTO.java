package com.br.minhasfinansas.domain.usuario;

public record UsuarioDTO(String email,String nome,String senha) {
    public UsuarioDTO(Usuario usuario) {
        this(usuario.getEmail(),usuario.getNome(),usuario.getSenha());
    }
}

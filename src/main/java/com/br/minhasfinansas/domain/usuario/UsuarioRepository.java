package com.br.minhasfinansas.domain.usuario;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Long>{
    boolean existsByEmail(String email);
    Optional<Usuario> findByEmail(String email);
}

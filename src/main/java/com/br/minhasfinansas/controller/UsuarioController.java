package com.br.minhasfinansas.controller;

import com.br.minhasfinansas.domain.lancamentos.LancamentoService;
import com.br.minhasfinansas.domain.usuario.Usuario;
import com.br.minhasfinansas.domain.usuario.UsuarioDTO;
import com.br.minhasfinansas.domain.usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private LancamentoService lancamentoService;

    @PostMapping("/autenticar")
    public ResponseEntity autenticar(@RequestBody UsuarioDTO usuarioDTO){
        Usuario usuarioAutenticado = usuarioService.autenticar(usuarioDTO.email(), usuarioDTO.senha());
        return ResponseEntity.ok(usuarioAutenticado);
    }

    @PostMapping
    @Transactional
    public ResponseEntity salvar(@RequestBody UsuarioDTO usuarioDTO){
        Usuario usuario = Usuario.builder().nome(usuarioDTO.nome())
                .email(usuarioDTO.email())
                .senha(usuarioDTO.senha())
                .data_cadastro(LocalDate.now())
                .build();
        usuarioService.salvarUsuario(usuario);
        return new ResponseEntity(usuarioDTO, HttpStatus.CREATED);
    }

    @GetMapping("{id}/saldo")
    public ResponseEntity obtersaldo(@PathVariable("id") Long id){

        Double saldo = lancamentoService.obterSaldoPorUsuario(id);
        return ResponseEntity.ok(saldo);
    }
}

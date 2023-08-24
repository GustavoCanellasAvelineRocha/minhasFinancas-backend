package com.br.minhasfinansas.controller;

import com.br.minhasfinansas.domain.lancamentos.*;
import com.br.minhasfinansas.domain.usuario.Usuario;
import com.br.minhasfinansas.domain.usuario.UsuarioService;
import com.br.minhasfinansas.domain.usuario.validacoes.RegraNegocioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentoController {
    @Autowired
    private LancamentoService lancamentoService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    @Transactional
    public ResponseEntity salvar(@RequestBody LancamentoDTO lancamentoDTO){
        Lancamento lancamento = new Lancamento(null,lancamentoDTO.descricao(),lancamentoDTO.mes(),lancamentoDTO.ano(),lancamentoDTO.valor(),
                TipoLancamento.valueOf(lancamentoDTO.tipo()), StatusLancamento.PENDENTE,
                usuarioService.findById(lancamentoDTO.idUsuario()), LocalDate.now());

        LancamentoDTO lancamentoResponse = new LancamentoDTO(lancamentoService.salvar(lancamento));
        return new ResponseEntity(lancamento, HttpStatus.CREATED);
    }

    @GetMapping("/buscar")
    public ResponseEntity buscar(@RequestParam(value = "descricao", required = false)String descricao,
                                 @RequestParam(value = "mes", required = false )Integer mes,
                                 @RequestParam(value = "ano", required = false)Integer ano,
                                 @RequestParam("usuario")Long idUsuario){
        Lancamento lancamentofiltro = new Lancamento();
        lancamentofiltro.setDescricao(descricao);
        if(mes != null) lancamentofiltro.setMes(mes);
        if(ano != null) lancamentofiltro.setAno(ano);
        Usuario usuario = usuarioService.findById(idUsuario);
        lancamentofiltro.setUsuario(usuario);
        List<Lancamento> lancamentos = lancamentoService.buscar(lancamentofiltro);
        return ResponseEntity.ok(lancamentos);
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity buscarPeloId(@PathVariable("id") Long id){
        Lancamento lancamento = lancamentoService.findById(id);

        LancamentoDTO lancamentoDTO= new LancamentoDTO(lancamento);

        return ResponseEntity.ok(lancamentoDTO);
    }

    @GetMapping("/listar")
    public ResponseEntity listar(){
        List<Lancamento> lista = lancamentoService.findAll();
        var listaDTO = lista.stream().map(LancamentoDTO::new).toList();

        return ResponseEntity.ok(listaDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id,@RequestBody LancamentoDTO lancamentoDTO){
        Lancamento entidade = lancamentoService.findById(id);
        Lancamento lancamento = new Lancamento(null,lancamentoDTO.descricao(),lancamentoDTO.mes(),lancamentoDTO.ano(),lancamentoDTO.valor(),
                TipoLancamento.valueOf(lancamentoDTO.tipo()), StatusLancamento.valueOf(lancamentoDTO.status()),
                usuarioService.findById(lancamentoDTO.idUsuario()), LocalDate.now());
        lancamento.setId(entidade.getId());
        lancamentoService.atualizar(lancamento);

        LancamentoDTO lancamentoResponse = new LancamentoDTO(lancamento);
        return ResponseEntity.ok(lancamentoResponse);
    }

    @PutMapping("{id}/atualiza-status")
    public ResponseEntity atualizarStatus(@PathVariable("id") Long id,@RequestBody StatusDTO lancamentoDTO){
        if(Objects.equals(lancamentoDTO.status(), StatusLancamento.PENDENTE.toString())
                || Objects.equals(lancamentoDTO.status(), StatusLancamento.CANCELADO.toString())
                || Objects.equals(lancamentoDTO.status(), StatusLancamento.EFETIVADO.toString())){
            var lancamento = lancamentoService.findById(id);
            lancamentoService.atualizarStatus(lancamento,StatusLancamento.valueOf(lancamentoDTO.status()));

            LancamentoDTO lancamentoResponse = new LancamentoDTO(lancamento);
            return ResponseEntity.ok(lancamentoResponse);
        } else throw new RegraNegocioException("Status invalido");
    }

    @DeleteMapping("{id}")
    public ResponseEntity deletar(@PathVariable("id") Long id){
        Lancamento lancamento = lancamentoService.findById(id);

        lancamentoService.deletar(lancamento);

        return ResponseEntity.ok().build();
    }
}

package com.br.minhasfinansas.controller;

import com.br.minhasfinansas.domain.lancamentos.LancamentoService;
import com.br.minhasfinansas.domain.usuario.Usuario;
import com.br.minhasfinansas.domain.usuario.UsuarioDTO;
import com.br.minhasfinansas.domain.usuario.UsuarioService;
import com.br.minhasfinansas.domain.usuario.validacoes.ErroDeAutenticacao;
import com.br.minhasfinansas.domain.usuario.validacoes.RegraNegocioException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

@ActiveProfiles("test")
@WebMvcTest(controllers = UsuarioController.class)
@AutoConfigureMockMvc
public class UsuarioControllerTest {

    final String API = "/api/usuarios";
    final MediaType Json = MediaType.APPLICATION_JSON;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UsuarioService usuarioService;

    @MockBean
    LancamentoService lancamentoService;

    @Test
    public void verificaSeAutenticouUmUsuario() throws Exception{
        UsuarioDTO dto = new UsuarioDTO("usuario@email.com","usuario","123");
        Usuario usuario = new Usuario(1L,"usuario","usuario@email.com","123", LocalDate.now());

        Mockito.when(usuarioService.autenticar("usuario@email.com","123")).thenReturn(usuario);

        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(API.concat("/autenticar"))
                .accept(Json)
                .contentType(Json)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(usuario.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()))
                .andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()));


    }

    @Test
    public void verificaSeHouveBadRequestAoAutenticarUmUsuario() throws Exception{
        UsuarioDTO dto = new UsuarioDTO("usario@email.com","usuario","123");

        Mockito.when(usuarioService.autenticar("usario@email.com","123")).thenThrow(ErroDeAutenticacao.class);

        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(API.concat("/autenticar"))
                .accept(Json)
                .contentType(Json)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());


    }

    @Test
    public void verificaSeCriouUmNovoUsuario() throws Exception{
        UsuarioDTO dto = new UsuarioDTO("usuario@email.com","usuario","123");
        Usuario usuario = new Usuario(1L,"usuario","usuario@email.com","123", LocalDate.now());

        Mockito.when(usuarioService.salvarUsuario(Mockito.any(Usuario.class))).thenReturn(usuario);

        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(API)
                .accept(Json)
                .contentType(Json)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()))
                .andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()));


    }

    @Test
    public void verificaSeHouveBadRequestAoCriarNovoUsuario() throws Exception{
        UsuarioDTO dto = new UsuarioDTO("usuario@email.com","usuario","123");
        Usuario usuario = new Usuario(1L,"usuario","usuario@email.com","123", LocalDate.now());

        Mockito.when(usuarioService.salvarUsuario(Mockito.any(Usuario.class))).thenThrow(RegraNegocioException.class);

        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(API)
                .accept(Json)
                .contentType(Json)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}

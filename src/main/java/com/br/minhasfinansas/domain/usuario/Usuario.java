package com.br.minhasfinansas.domain.usuario;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "usuario",schema = "financas")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Usuario {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    @JsonIgnore
    private String senha;
    private LocalDate data_cadastro;
}

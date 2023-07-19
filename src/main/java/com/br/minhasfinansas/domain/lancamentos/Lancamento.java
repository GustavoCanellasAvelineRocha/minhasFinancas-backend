package com.br.minhasfinansas.domain.lancamentos;

import com.br.minhasfinansas.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.time.LocalDate;

@Entity
@Table(name = "lancamento", schema = "financas")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Lancamento {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;
    private Integer mes;
    private Integer ano;
    private Double valor;
    @Enumerated(value = EnumType.STRING)
    private TipoLancamento tipo;
    @Enumerated(value = EnumType.STRING)
    private StatusLancamento status;
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
    @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
    private LocalDate data_cadastro;
}

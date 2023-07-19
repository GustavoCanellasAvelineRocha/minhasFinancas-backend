package com.br.minhasfinansas.domain.lancamentos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LancamentoRepository extends JpaRepository<Lancamento,Long> {
    @Query(value = " SELECT SUM(l.valor) " +
            " FROM Lancamento l " +
            " JOIN l.usuario u " +
            " WHERE u.id = :idUsuario AND l.tipo = :tipo AND l.status = :status" +
            " GROUP BY u ")
    Double obterSaldoPorTipoLancamentoEUsuario(@Param("idUsuario") Long idUsuario, @Param("tipo") TipoLancamento tipo, @Param("status") StatusLancamento status);



}

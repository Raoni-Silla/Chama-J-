package com.raoni.chamaja.repository;

import com.raoni.chamaja.model.Prestador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PrestadorRepository extends JpaRepository<Prestador, Long> {
    @Query(value = "SELECT DISTINCT p.id FROM prestador p " +
            "JOIN usuarios u ON p.id = u.id " +
            "LEFT JOIN endereco e ON e.usuario_id = u.id " +
            "LEFT JOIN prestador_categoria pc ON pc.prestador_id = p.id " +
            "LEFT JOIN categoria c ON c.id = pc.categoria_id " +
            "WHERE to_tsvector('portuguese', coalesce(u.nome, '') || ' ' || coalesce(e.nome_cidade, '') || ' ' || coalesce(c.nome, '')) " +
            "@@ websearch_to_tsquery('portuguese', :termo)",
            nativeQuery = true)
    List<Long> findIdsByBuscaInteligente(@Param("termo") String termo);

    List<Prestador> findTop5ByOrderByNotaMediaDesc();
}

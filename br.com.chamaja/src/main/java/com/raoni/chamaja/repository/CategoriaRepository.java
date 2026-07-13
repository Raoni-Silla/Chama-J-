package com.raoni.chamaja.repository;

import com.raoni.chamaja.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoriaRepository extends JpaRepository <Categoria, Long> {

    @Query(value = "SELECT * FROM categoria ORDER BY RANDOM() LIMIT 6", nativeQuery = true)
    List<Categoria> find6CategoriasAleatorias();

}

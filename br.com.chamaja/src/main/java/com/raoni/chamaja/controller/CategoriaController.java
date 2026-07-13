package com.raoni.chamaja.controller;

import com.raoni.chamaja.dto.Categoria.CategoriaDetalhesDTO;
import com.raoni.chamaja.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping(path = "/obter-6-categorias")
    @PreAuthorize("hasRole('USUARIO')")
    public ResponseEntity<List<CategoriaDetalhesDTO>> obterSeisCategorias (){
        List<CategoriaDetalhesDTO> responseDTOS = categoriaService.obterSeisCategoriasAleatorias();
        return ResponseEntity.ok(responseDTOS);
    }

}

package com.raoni.chamaja.controller;

import com.raoni.chamaja.dto.Usuario.UsuarioInfoBasicasDTO;
import com.raoni.chamaja.dto.Usuario.UsuarioInfoPerfilDTO;
import com.raoni.chamaja.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping(path = "/api/usuarios")
@RestController
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping(path = "obter-infos-basicas")
    @PreAuthorize("hasRole('USUARIO')")
    public ResponseEntity<UsuarioInfoBasicasDTO> obterInfosBasicasUsuarioLogado(){
        UsuarioInfoBasicasDTO resposta = usuarioService.obterNomeAndEnderecoDoUsuarioLogado();
        return ResponseEntity.ok(resposta);
    }

    @GetMapping(path = "obter-infos-perfil")
    @PreAuthorize("hasRole('USUARIO')")
    public ResponseEntity<UsuarioInfoPerfilDTO> obterInfosParaTelaDePerfil(){
        UsuarioInfoPerfilDTO resposta = usuarioService.obterInformacoesDoPerfilUsuario();
        return ResponseEntity.ok(resposta);
    }
}

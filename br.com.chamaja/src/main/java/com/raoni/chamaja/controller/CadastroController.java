package com.raoni.chamaja.controller;

import com.raoni.chamaja.dto.Cadastro.CadastroInicialRequestDTO;
import com.raoni.chamaja.dto.Cadastro.CadastroResponseDTO;
import com.raoni.chamaja.dto.usuario.UsuarioResponseDTO;
import com.raoni.chamaja.service.CadastroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registro")
@RequiredArgsConstructor
public class CadastroController {

    private final CadastroService cadastroService;

    @PostMapping("/iniciar")
    public ResponseEntity<CadastroResponseDTO> iniciarCadastro(
            @RequestBody CadastroInicialRequestDTO dto) {
        CadastroResponseDTO response = cadastroService.iniciarCadastro(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/telefone")
    public ResponseEntity<CadastroResponseDTO> adicionarTelefone(
            @RequestParam Long id, @RequestParam String telefone) {
       CadastroResponseDTO response = cadastroService.adicionarTelefone(id,telefone);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/telefone/solicitarenvio")
    public ResponseEntity<CadastroResponseDTO> solicitarEnvioSMS(
            @RequestParam Long id) {
        CadastroResponseDTO response = cadastroService.solicitarEnvioSms(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/telefone/confirmarcodigo")
    public ResponseEntity<CadastroResponseDTO> confirmarCodigoSMS(
            @RequestParam Long id, @RequestParam String codigoDigitadoPeloUsuario) {
        CadastroResponseDTO response = cadastroService.confirmarCodigoSms(id,codigoDigitadoPeloUsuario);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/tipousuario")
    public ResponseEntity<UsuarioResponseDTO> tipoUsuario(
            @RequestParam Long id, @RequestParam String tipoUsuario) {
        UsuarioResponseDTO response = cadastroService.usuarioOuPrestador(id,tipoUsuario);
        return ResponseEntity.ok(response);
    }



}

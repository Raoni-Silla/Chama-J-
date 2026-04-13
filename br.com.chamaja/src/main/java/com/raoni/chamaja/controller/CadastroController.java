package com.raoni.chamaja.controller;

import com.raoni.chamaja.dto.Cadastro.CadastroInicialRequestDTO;
import com.raoni.chamaja.dto.Cadastro.CadastroResponseDTO;
import com.raoni.chamaja.model.CadastroTemporario;
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

        CadastroTemporario cadastro = cadastroService.iniciarCadastro(dto);

        CadastroResponseDTO response = new CadastroResponseDTO(
                cadastro.getId(),
                cadastro.getStatus().name()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/telefone")
    public ResponseEntity<CadastroResponseDTO> adicionarTelefone(
            @RequestParam Long id, @RequestParam String telefone) {

       CadastroTemporario cadastro = cadastroService.adicionarTelefone(id,telefone);

        CadastroResponseDTO response = new CadastroResponseDTO(
                cadastro.getId(),
                cadastro.getStatus().name()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/telefone/solicitarenvio")
    public ResponseEntity<CadastroResponseDTO> solicitarEnvioSMS(
            @RequestParam Long id) {

        CadastroTemporario cadastro = cadastroService.solicitarEnvioSms(id);

        CadastroResponseDTO response = new CadastroResponseDTO(
                cadastro.getId(),
                cadastro.getStatus().name()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/telefone/confirmarcodigo")
    public ResponseEntity<CadastroResponseDTO> solicitarEnvioSMS(
            @RequestParam Long id, @RequestParam String codigoDigitadoPeloUsuario) {

        CadastroTemporario cadastro = cadastroService.confirmarCodigoSms(id,codigoDigitadoPeloUsuario);

        CadastroResponseDTO response = new CadastroResponseDTO(
                cadastro.getId(),
                cadastro.getStatus().name()
        );

        return ResponseEntity.ok(response);
    }
    @PostMapping("/tipousuario")
    public ResponseEntity<CadastroResponseDTO> tipoUsuario(
            @RequestParam Long id, @RequestParam String tipoUsuario) {

        CadastroTemporario cadastro = cadastroService.usuarioOuPrestador(id,tipoUsuario);

        CadastroResponseDTO response = new CadastroResponseDTO(
                cadastro.getId(),
                cadastro.getStatus().name()
        );

        return ResponseEntity.ok(response);
    }



}

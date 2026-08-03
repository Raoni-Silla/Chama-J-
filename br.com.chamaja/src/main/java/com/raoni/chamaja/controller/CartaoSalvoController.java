package com.raoni.chamaja.controller;

import com.raoni.chamaja.dto.Cartao.CartaoSalvoRequestDTO;
import com.raoni.chamaja.dto.Cartao.CartaoSalvoResponseDTO;
import com.raoni.chamaja.model.CartaoSalvo;
import com.raoni.chamaja.service.CartaoSalvoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cartoes")
@RequiredArgsConstructor
public class CartaoSalvoController {

    private final CartaoSalvoService cartaoSalvoService;


    @PostMapping("/criar")
    @PreAuthorize("hasRole('USUARIO')")
    public ResponseEntity<Void> criar(
            @Valid @RequestBody CartaoSalvoRequestDTO request
    ) {
        cartaoSalvoService.salvarCartao(
                request.token()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping(path = "/listar")
    @PreAuthorize("hasRole('USUARIO')")
    public ResponseEntity <List<CartaoSalvoResponseDTO>> listarCartoes () {
        List<CartaoSalvoResponseDTO> dtos = cartaoSalvoService.listarCartoesSalvos();
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }

    @DeleteMapping("/{idCartao}")
    @PreAuthorize("hasRole('USUARIO')")
    public ResponseEntity<Void> excluirCartao(
            @PathVariable Long idCartao
    ) {
        cartaoSalvoService.excluirCartao(idCartao);

        return ResponseEntity.noContent().build();
    }

}

package com.raoni.chamaja.controller;

import com.raoni.chamaja.dto.Login.LoginRequestDTO;
import com.raoni.chamaja.dto.Login.LoginResponseDTO;
import com.raoni.chamaja.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LoginController {

    private final LoginService loginService;

    @PostMapping
    public ResponseEntity<LoginResponseDTO> logar(@RequestBody LoginRequestDTO dto) {
        LoginResponseDTO response = loginService.autenticar(dto);
        return ResponseEntity.ok(response);
    }
}
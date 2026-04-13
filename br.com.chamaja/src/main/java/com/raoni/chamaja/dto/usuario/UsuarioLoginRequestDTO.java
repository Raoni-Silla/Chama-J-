package com.raoni.chamaja.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioLoginRequestDTO(
        @Email
        @NotBlank
        String email,
        @NotBlank
        String senha
) {

}

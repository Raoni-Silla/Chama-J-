package com.raoni.chamaja.dto.Usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UsuarioTrocaSenhaDTO(
        String senhaAntiga,
        @NotBlank(message = "A senha não pode ser nula")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#_-])[A-Za-z\\d@$!%*?&#_-]{8,}$",
                message = "A senha deve ter no mínimo 8 caracteres, incluindo maiúscula, minúscula, número e caractere especial"
        )
        String senhaNova
) {
}

package com.raoni.chamaja.dto.usuario;

import com.raoni.chamaja.enums.TipoUsuario;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(
        @NotBlank
        @Size(min = 3 , max = 100)
        String nome,
        @Email
        @NotBlank
        @Column(unique = true)
        String email,
        TipoUsuario roleUsuario,
        Double notaMedia,
        @PositiveOrZero(message = "O raio de atuação não pode ser negativo")
        Long raioAtuacao
) {
}

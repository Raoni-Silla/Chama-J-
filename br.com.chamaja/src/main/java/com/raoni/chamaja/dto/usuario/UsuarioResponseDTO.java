package com.raoni.chamaja.dto.usuario;

import com.raoni.chamaja.enums.TipoUsuario;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record UsuarioResponseDTO(

        String nome,
        @Column(unique = true)
        String email,
        TipoUsuario roleUsuario,
        Double notaMedia,
        @PositiveOrZero(message = "O raio de atuação não pode ser negativo")
        Long raioAtuacao
) {
}

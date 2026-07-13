package com.raoni.chamaja.dto.Usuario;

import com.raoni.chamaja.enums.TipoUsuario;

public record UsuarioResponseDTO(

        String nome,
        String email,
        TipoUsuario roleUsuario,
        Double notaMedia,
        Long raioAtuacao,
        String tokenDefinitivo
) {
}

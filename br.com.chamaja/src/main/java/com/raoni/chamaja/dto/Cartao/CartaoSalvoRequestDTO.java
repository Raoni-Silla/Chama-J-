package com.raoni.chamaja.dto.Cartao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CartaoSalvoRequestDTO(

        @NotBlank
        String token

) {
}

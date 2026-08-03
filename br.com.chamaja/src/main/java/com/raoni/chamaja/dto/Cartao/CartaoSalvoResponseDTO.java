package com.raoni.chamaja.dto.Cartao;

public record CartaoSalvoResponseDTO(
        Long id,
        String nomeTitular,
        String ultimosDigitos,
        String mesAnoVencimento,
        String bandeira
) {
}

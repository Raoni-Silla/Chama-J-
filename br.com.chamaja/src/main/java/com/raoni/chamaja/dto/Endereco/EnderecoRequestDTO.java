package com.raoni.chamaja.dto.Endereco;

public record EnderecoRequestDTO (
        String logradouro,
        Long numero,
        String complemento,
        Boolean enderecoPrincipal,
        String nomeCidade,
        String siglaEstado,
        String cep,
        Double latitude,
        Double longitude

){
}

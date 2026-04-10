package com.raoni.chamaja.enums;
public enum TipoTransacao {
    ENTRADA_SERVICO,    // Dinheiro que entrou de um job
    SAQUE_PIX,          // Dinheiro que o prestador tirou do app
    ESTORNO,            // Caso um serviço seja cancelado
    TAXA_PLATAFORMA     // Se o ChamaJá cobrar uma comissão
}
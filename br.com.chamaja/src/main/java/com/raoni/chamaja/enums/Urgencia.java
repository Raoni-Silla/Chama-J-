package com.raoni.chamaja.enums;

public enum Urgencia {
    DE_BOA(1.0),       // Sem taxa extra
    O_QUANTO_ANTES(1.2), // 20% de acréscimo, por exemplo
    PRA_JA(1.5);       // 50% de acréscimo pelo "pânico"

    private final double multiplicador;

    Urgencia(double multiplicador) {
        this.multiplicador = multiplicador;
    }

    public double getMultiplicador() {
        return multiplicador;
    }
}
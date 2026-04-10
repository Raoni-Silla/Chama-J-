package com.raoni.chamaja.model;

import com.raoni.chamaja.enums.StatusPagamento;
import com.raoni.chamaja.enums.MetodoPagamento;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MetodoPagamento metodoPagamento;

    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    private StatusPagamento status;

    private LocalDateTime dataPagamento;
}
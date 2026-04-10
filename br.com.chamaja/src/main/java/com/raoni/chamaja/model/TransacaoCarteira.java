package com.raoni.chamaja.model;

import com.raoni.chamaja.enums.TipoTransacao;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransacaoCarteira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "carteira_id", nullable = false)
    private Carteira carteira;

    @Column(precision = 19, scale = 2)
    private BigDecimal valorTransacao;

    @Enumerated(EnumType.STRING)
    private TipoTransacao tipoTransacao;

    private LocalDateTime dataTransacao;

    private String descricao;

    @PrePersist
    public void prePersist() {
        this.dataTransacao = LocalDateTime.now();
    }
}

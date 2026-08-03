package com.raoni.chamaja.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartaoSalvo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @NotBlank
    private String nomeTitular;

    @NotBlank
    @Size(min = 4, max = 4)
    private String ultimosDigitos;

    @NotBlank
    private String bandeira;

    @Column(nullable = false)
    private String mercadoPagoCardId;

    @NotBlank
    private String mesAnoVencimento;

    private boolean cartaoPrincipal;
}

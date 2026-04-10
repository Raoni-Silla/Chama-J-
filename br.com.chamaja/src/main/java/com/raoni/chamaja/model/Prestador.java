package com.raoni.chamaja.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Prestador extends Usuario {

    @NotBlank
    private String biografia;

    private Long servicosConcluidos;

    @PositiveOrZero(message = "O valor da hora não pode ser negativo")
    private Double valorHora;

    private Integer tempoDeRespostaMedio;

    @ManyToMany
    @JoinTable(
            name = "prestador_categoria",
            joinColumns = @JoinColumn(name = "prestador_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    @Size(min = 1, message = "O prestador deve possuir ao menos uma categoria de especialidade")
    private List<Categoria> categorias;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "carteira_id", referencedColumnName = "id")
    private Carteira carteira;

    @OneToMany(mappedBy = "prestador")
    private List<Proposta> propostas;

    @OneToMany(mappedBy = "prestador")
    private List<Chamado> chamados;
}
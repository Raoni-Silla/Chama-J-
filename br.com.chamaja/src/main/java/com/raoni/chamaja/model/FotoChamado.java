package com.raoni.chamaja.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FotoChamado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @URL
    @NotBlank
    private String url;

    @ManyToOne
    @JoinColumn(name = "chamado_id")
    private Chamado chamado; // FK para o chamado dono da foto
}
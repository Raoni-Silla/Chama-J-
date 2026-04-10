package com.raoni.chamaja.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2)
    private String nome;

    @NotBlank
    @URL
    private String iconUrl;

    @ManyToMany(mappedBy = "categorias")
    private List<Chamado> chamados;
}

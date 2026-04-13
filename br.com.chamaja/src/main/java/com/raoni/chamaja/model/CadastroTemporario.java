package com.raoni.chamaja.model;

import com.raoni.chamaja.enums.StatusCadastro;
import com.raoni.chamaja.enums.TipoUsuario;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class CadastroTemporario {

    @Id
    @GeneratedValue
    private Long id;

    private String nome;

    private String cpf;

    private LocalDate dataNascimento;

    private String email;

    private String senha;

    private String telefone;

    private Boolean telefoneValidado;

    private String codigoSms;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipoUsuario;

    @Enumerated(EnumType.STRING)
    private StatusCadastro status;
}
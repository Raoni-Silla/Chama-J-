package com.raoni.chamaja.model;

import com.raoni.chamaja.enums.StatusCadastro;
import com.raoni.chamaja.enums.TipoUsuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class CadastroTemporario {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Size(min = 3 , max = 100)
    private String nome;

    @NotBlank
    @Column(unique = true)
    @Pattern(
            regexp = "^\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}$",
            message = "CPF inválido"
    )
    private String cpf;

    private LocalDate dataNascimento;

    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    @NotBlank
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "A senha deve ter no mínimo 8 caracteres, incluindo maiúscula, minúscula, número e caractere especial"
    )
    private String senha;

    private String telefone;

    private Boolean telefoneValidado;

    private String codigoSms;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipoUsuario;

    @Enumerated(EnumType.STRING)
    private StatusCadastro status;
}
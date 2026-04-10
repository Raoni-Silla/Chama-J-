package com.raoni.chamaja.model;

import com.raoni.chamaja.enums.TipoUsuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3 , max = 100)
    private String nome;

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

    @NotBlank
    @Column(unique = true)
    @Pattern(
            regexp = "^\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}$",
            message = "CPF inválido"
    )
    private String cpf;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipoUsuario;

    private LocalDateTime dataCriacao;

    private boolean verificado;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Endereco> enderecos;

    @PrePersist
    public void prePersist() {
        this.dataCriacao = LocalDateTime.now();
        this.verificado = false;
    }

}
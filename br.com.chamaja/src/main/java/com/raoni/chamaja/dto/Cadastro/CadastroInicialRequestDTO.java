package com.raoni.chamaja.dto.Cadastro;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;


public record CadastroInicialRequestDTO(
        @NotBlank (message = "O nome não pode ser nulo")
        @Size(min = 3 , max = 100)
        String nome,
        @NotBlank (message = "O cpf não pode ser nulo")
        @Pattern(
                regexp = "^\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}$",
                message = "CPF inválido"
        )
        String cpf,
        LocalDate dataNascimento,
        @Email
        @NotBlank
        String email,
        @NotBlank(message = "A senha não pode ser nula")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "A senha deve ter no mínimo 8 caracteres, incluindo maiúscula, minúscula, número e caractere especial"
        )
        String senha
) {}
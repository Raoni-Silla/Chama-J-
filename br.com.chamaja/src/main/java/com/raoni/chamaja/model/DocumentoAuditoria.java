package com.raoni.chamaja.model;

import com.raoni.chamaja.enums.StatusAuditoria;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DocumentoAuditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @NotBlank
    @URL
    private String urlDoDocumento;

    private String tipoDocumento; // Ex: "RG", "CNPJ", "ANTECEDENTES_CRIMINAIS"

    @Enumerated(EnumType.STRING)
    private StatusAuditoria statusAuditoria;

    @Column(columnDefinition = "TEXT")
    private String observacaoAdmin; // Motivo de uma eventual reprovação

    private LocalDateTime dataEnvio;
    private LocalDateTime dataAnalise;

    @PrePersist
    public void prePersist() {
        this.dataEnvio = LocalDateTime.now();
        this.statusAuditoria = StatusAuditoria.PENDENTE;
    }
}
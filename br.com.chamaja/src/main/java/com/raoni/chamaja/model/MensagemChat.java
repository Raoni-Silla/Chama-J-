package com.raoni.chamaja.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MensagemChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chamado_id", nullable = false)
    private Chamado chamado; // A qual conversa isso pertence

    @ManyToOne
    @JoinColumn(name = "remetente_id", nullable = false)
    private Usuario remetente; // Quem enviou (pode ser o Cliente ou Prestador)

    @Column(columnDefinition = "TEXT", nullable = false)
    private String conteudo;

    private LocalDateTime dataEnvio;

    private boolean lida;

    @PrePersist
    public void prePersist() {
        this.dataEnvio = LocalDateTime.now();
        this.lida = false;
    }
}
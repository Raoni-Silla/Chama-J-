package com.raoni.chamaja.repository;

import com.raoni.chamaja.model.CartaoSalvo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CartaoSalvoRepository
        extends JpaRepository<CartaoSalvo, Long> {

    List<CartaoSalvo> findAllByUsuario_Id(Long idUsuario);

    Long id(Long id);

    Optional <CartaoSalvo> findByIdAndUsuario_Id(Long idCartao, Long idUsuario);
}
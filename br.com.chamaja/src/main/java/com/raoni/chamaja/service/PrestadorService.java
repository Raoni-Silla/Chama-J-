package com.raoni.chamaja.service;

import com.raoni.chamaja.dto.Prestador.MelhoresDoMesDTO;
import com.raoni.chamaja.dto.Prestador.PrestadorResponseDTO;
import com.raoni.chamaja.model.Categoria;
import com.raoni.chamaja.model.Endereco;
import com.raoni.chamaja.model.Prestador;
import com.raoni.chamaja.repository.PrestadorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrestadorService {

    private final PrestadorRepository prestadorRepository;

    private PrestadorResponseDTO converterParaDTO(Prestador prestador) {

        String cidade = prestador.getEnderecos().stream()
                .filter(Endereco::isEnderecoPrincipal)
                .map(Endereco::getNomeCidade)
                .findFirst()
                .orElse("Cidade não informada");

        // Pega só os nomes das categorias
        List<String> nomeCategorias = prestador.getCategorias().stream()
                .map(Categoria::getNome)
                .collect(Collectors.toList());

        return new PrestadorResponseDTO(
                prestador.getId(),
                prestador.getNome(),
                prestador.getFotoUrl(),
                prestador.getBiografia(),
                prestador.getNotaMedia(),
                prestador.getValorHora(),
                cidade,
                nomeCategorias
        );
    }

    private MelhoresDoMesDTO converterDtoMelhorMes (Prestador prestador){

        List<String> nomeCategorias = prestador.getCategorias().stream()
                .map(Categoria::getNome)
                .toList();

        return new MelhoresDoMesDTO(
                prestador.getId(),
                prestador.getNome(),
                prestador.getFotoUrl(),
                nomeCategorias,
                prestador.getNotaMedia(),
                prestador.isVerificado(),
                prestador.getServicosConcluidos()
        );

    }

    public List<PrestadorResponseDTO> buscarPrestadores(String termo) {
        List<Prestador> prestadores;


        if (termo == null || termo.trim().isEmpty()) {
            prestadores = prestadorRepository.findAll();
        } else {
            List<Long> ids = prestadorRepository.findIdsByBuscaInteligente(termo);
            if (ids.isEmpty()) return List.of();
            prestadores = prestadorRepository.findAllById(ids);
        }

        return prestadores.stream().map(this::converterParaDTO).collect(Collectors.toList());
    }


    public PrestadorResponseDTO detalharInformacoesPrestador(Long id) {
        if (id == null || id <= 0){
            throw new IllegalArgumentException("Impossivel buscar um id nulo");
        }
        Prestador prestador = prestadorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Impossivel encontrar esse prestador"));
        return converterParaDTO(prestador);
    }

    public List<MelhoresDoMesDTO> top5MelhoresPrestadores () {
        return prestadorRepository.findTop5ByOrderByNotaMediaDesc().stream().map(this::converterDtoMelhorMes).toList();
    }
}

package com.raoni.chamaja.service;

import com.raoni.chamaja.dto.Usuario.UsuarioInfoBasicasDTO;
import com.raoni.chamaja.dto.Usuario.UsuarioInfoPerfilDTO;
import com.raoni.chamaja.model.Endereco;
import com.raoni.chamaja.model.Usuario;
import com.raoni.chamaja.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository userRepo;

    private Long obterIdUsuarioLogado (){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return Long.parseLong(authentication.getName());
    }

    public UsuarioInfoBasicasDTO obterNomeAndEnderecoDoUsuarioLogado (){
        Long idSeguro = obterIdUsuarioLogado();
        Usuario usuario = userRepo.findById(idSeguro).orElseThrow(() -> new RuntimeException("Impossivel encontrar esse usuario"));
        String nome = usuario.getNome();
        String cidadePrincipal = usuario.getEnderecos().stream()
                .filter(Endereco::isEnderecoPrincipal)
                .map(Endereco::getNomeCidade)
                .findFirst()
                .orElse("Localização não informada");
        String estado = usuario.getEnderecos().stream()
                .filter(Endereco::isEnderecoPrincipal)
                .map(Endereco::getSiglaEstado)
                .findFirst()
                .orElse("Localização não informada");
        return new UsuarioInfoBasicasDTO(nome, cidadePrincipal, estado);
    }


    public UsuarioInfoPerfilDTO obterInformacoesDoPerfilUsuario(){
        Long idSeguro = obterIdUsuarioLogado();
        Usuario usuario = userRepo.findById(idSeguro).orElseThrow(() -> new RuntimeException("Impossivel encontrar esse usuario"));
        return new UsuarioInfoPerfilDTO(usuario.getNome(), usuario.getEmail(), usuario.getTelefone(), usuario.getFotoUrl(), usuario.getCpf());
    }

}

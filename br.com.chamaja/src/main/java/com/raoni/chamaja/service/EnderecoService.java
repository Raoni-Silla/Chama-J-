package com.raoni.chamaja.service;

import com.raoni.chamaja.dto.Endereco.EnderecoRequestDTO;
import com.raoni.chamaja.dto.Endereco.EnderecoResponseDTO;
import com.raoni.chamaja.model.Endereco;
import com.raoni.chamaja.model.Usuario;
import com.raoni.chamaja.repository.EnderecoRepository;
import com.raoni.chamaja.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnderecoService {

    private final UsuarioRepository userRepo;
    private final EnderecoRepository enderecoRepo;

    private Long extrairIdUsuarioLogado (){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return Long.parseLong(authentication.getName());
    }

    public void salvarEndereco(EnderecoRequestDTO dto) {

        Usuario usuario = userRepo.findById(extrairIdUsuarioLogado()).orElseThrow(() -> new EntityNotFoundException("impossivel encontrar esse usuario"));
        Endereco endereco = new Endereco();
        endereco.setEnderecoPrincipal(dto.enderecoPrincipal());
        endereco.setCep(dto.cep());
        endereco.setComplemento(dto.complemento());
        endereco.setNumero(dto.numero());
        endereco.setLogradouro(dto.logradouro());
        endereco.setLatitude(dto.latitude());
        endereco.setLongitude(dto.longitude());
        endereco.setNomeCidade(dto.nomeCidade());
        endereco.setSiglaEstado(dto.siglaEstado());
        usuario.getEnderecos().add(endereco);
        endereco.setUsuario(usuario);

        userRepo.save(usuario);

    }

    public List<EnderecoResponseDTO> listarEnderecos (){
        Long idUsuario = extrairIdUsuarioLogado();
        List<Endereco> enderecos = enderecoRepo.findByUsuarioId(idUsuario);
        return enderecos.stream()
                .map(end -> new EnderecoResponseDTO(
                        end.getId(),
                        end.getLogradouro(),
                        end.getNumero(),
                        end.getComplemento(),
                        end.getNomeCidade(),
                        end.getSiglaEstado(),
                        end.getCep(),
                        end.getLatitude(),
                        end.getLongitude(),
                        end.isEnderecoPrincipal()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public void excluirEndereco(Long id) {
        if (id == null || id == 0){
            throw new IllegalArgumentException("Id enviado é impossivel de encontrar");
        }
        enderecoRepo.deleteById(id);
    }

}

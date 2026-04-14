package com.raoni.chamaja.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.raoni.chamaja.dto.usuario.UsuarioResponseDTO;
import com.raoni.chamaja.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    //pega a chave de acesso
    @Value("${api.security.token.secret}")
    private String secretKey;

    // Método auxiliar para calcular a validade de 2 horas (no fuso horário do Brasil)
    private Instant dataExpiracao() {
        return Instant.now().plusSeconds(7200); // 2 horas
    }

    public String gerarToken(Usuario usuario){
        try {
            Algorithm algoritimoMatematicoUtilizado = Algorithm.HMAC256(secretKey);
            return JWT.create()
                    //header (quem está criando esse token?)
                    .withIssuer("API chama Já")
                    //PAYLOAD: De quem é esse crachá? (Geralmente usamos o e-mail ou login como assunto principal)
                    .withSubject(usuario.getEmail())
                    //PAYLOAD: Qual o perfil dele? (Aqui a gente diz se é ADMIN, PRESTADOR ou CLIENTE)
                    .withClaim("perfil", usuario.getTipoUsuario().name())
                    .withClaim("id", usuario.getId())
                    //PAYLOAD: Quando o crachá vence? (Ex: dura 2 horas)
                    .withExpiresAt(dataExpiracao())
                    //ASSINATURA: Carimba tudo com a chave secreta e gera a String final
                    .sign(algoritimoMatematicoUtilizado);
        }catch (JWTCreationException ex){
            throw new RuntimeException("Erro ao gerar token");
        }
    }

}

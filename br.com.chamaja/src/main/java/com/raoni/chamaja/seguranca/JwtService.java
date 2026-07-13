package com.raoni.chamaja.seguranca;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.raoni.chamaja.model.Usuario;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * essa classe minha seria a maquina que cria e le pulseiras
 * o segurança chama essa classe toda vez que precisar fabricar uma pulseira nova ou
 * ler uma pulseira ja criada
 */
@Service
public class JwtService {

    //é a minha senha secreta usada para assinar o token quando misturado o header e o payload
    private final String secretKey = "chave secreta e segura";

    public String gerarToken (String identificadorUsuario){
       try{

           Algorithm algorithm = Algorithm.HMAC256(secretKey);

           return JWT.create()
                   .withIssuer("chama-ja-api") //quem emitiu o token?
                   .withSubject(identificadorUsuario) //de quem pertence esse token?
                   .withExpiresAt(gerarDataExpiracao()) //até quando é valido?
                   .sign(algorithm); //criptografo como?
       }catch (JWTCreationException exception){
           throw new RuntimeException("ERRO AO GERAR O TOKEN JWT",exception);
       }
    }

    //quando o usuario faz login com sucesso, o spring chama esse metodo, entregando o objeto usuario
    public String gerarTokenDefinitivo(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            return JWT.create()
                    .withIssuer("chama-ja-api") //quem imprimiu esse token?
                    .withSubject(String.valueOf(usuario.getId())) // essa pulseira é de qual usuario?
                    .withClaim("ROLE", usuario.getTipoUsuario().name()) //é uma informação a mais adicionada no token e ele é prestador ou usuario
                    .withExpiresAt(gerarDataExpiracao()) // coloca a data de expiração
                    .sign(algorithm); //assina o token

        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar o token JWT", exception);
        }
    }

    //leitor de token, quando o usuario tenta acesar uma rota protegida, ele manda o token, o segurança pega o token e coloca nesse metodo
    public String extrairIdentificador(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            return JWT.require(algorithm)
                    .withIssuer("chama-ja-api")
                    .build()
                    .verify(token) //primeiro verifica se a assinatura foi feita com a chave secreta,vê se quem emitiu foi realmente minha api, verifica se a assinatura bate e se está valido o token
                    .getSubject();
        }catch (JWTVerificationException exception) {
            // Se a assinatura for falsa, se o token expirou ou se estiver malformado, cai aqui.
            throw new RuntimeException("Token JWT inválido ou expirado", exception);
        }
    }

    public String extrairRole(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            return JWT.require(algorithm)
                    .withIssuer("chama-ja-api")
                    .build()
                    .verify(token)
                    .getClaim("ROLE")
                    .asString();
        }catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT inválido ou expirado", exception);
        }
    }

    private Instant gerarDataExpiracao(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}

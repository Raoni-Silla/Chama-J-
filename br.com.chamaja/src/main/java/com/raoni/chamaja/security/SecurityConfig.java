package com.raoni.chamaja.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration //classes com essa anotations avisam o spring que quando ele iniciar ele deve seguir as instruções dadas por essa classe
@EnableWebSecurity
public class SecurityConfig {

    @Bean //o bean torna o objeto bcrypt auto gerenciado por ele
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // Regra 1: Desligando a proteção de formulários antigos (O escudo antigo)
                .csrf(AbstractHttpConfigurer::disable)

                // Regra 2: Política STATELESS (A amnésia proposital, sem guardar sessão)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Regra 3: A Lista VIP de rotas
                .authorizeHttpRequests(auth -> auth
                        // Liberamos o cadastro e o login para qualquer pessoa
                        .requestMatchers("/registro/**", "/login",  "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()

                        // Qualquer outra requisição vai precisar estar autenticada (ter o crachá)
                        .anyRequest().authenticated()
                )
                .build(); // Empacota tudo e entrega para o Spring
    }
}

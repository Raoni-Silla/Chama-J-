package com.raoni.chamaja.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration //classes com essa anotations avisam o spring que quando ele iniciar ele deve seguir as instruções dadas por essa classe
public class SecurityConfig {

    @Bean //o bean torna o objeto bcrypt auto gerenciado por ele
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}

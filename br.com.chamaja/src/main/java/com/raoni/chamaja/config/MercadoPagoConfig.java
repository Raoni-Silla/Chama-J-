package com.raoni.chamaja.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
public class MercadoPagoConfig {

    @Bean
    public RestClient mercadoPagoRestClient(
            @Value("${mercadopago.access-token}") String accessToken
    ) {
        return RestClient.builder()
                .baseUrl("https://api.mercadopago.com")
                .defaultHeader(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                )
                .defaultHeader(
                        HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON_VALUE
                )
                .build();
    }
}

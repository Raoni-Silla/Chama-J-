package com.raoni.chamaja.dto.MercadoPago;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MercadoPagoCardResponse(
        String id,

        @JsonProperty("expiration_month")
        Integer expirationMonth,

        @JsonProperty("expiration_year")
        Integer expirationYear,

        @JsonProperty("last_four_digits")
        String lastFourDigits,

        @JsonProperty("payment_method")
        PaymentMethod paymentMethod,

        Cardholder cardholder
) {

    public record PaymentMethod(
            String id,
            String name
    ) {
    }

    public record Cardholder(
            String name
    ) {
    }
}

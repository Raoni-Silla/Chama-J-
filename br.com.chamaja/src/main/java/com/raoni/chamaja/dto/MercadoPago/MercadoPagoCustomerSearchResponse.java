package com.raoni.chamaja.dto.MercadoPago;

import java.util.List;

public record MercadoPagoCustomerSearchResponse(
        List<MercadoPagoCustomerResponse> results
) {
}

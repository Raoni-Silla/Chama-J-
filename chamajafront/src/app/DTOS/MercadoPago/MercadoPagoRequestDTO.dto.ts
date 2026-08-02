export interface MercadoPagoRequestDTO {
    card_number: string,
    security_code: string,
    expiration_month: string,
    expiration_year: string,
    cardholder: {
        name: string,
        identification: {
            type : string,
            number: string
        }
    }
}
export interface MercadoPagoResponseDTO {
  id: string;
  public_key: string;
  cardholder: CardholderDTO;
  status: string;
  date_created: string;
  date_last_updated: string;
  date_due: string;
  luhn_validation: boolean;
  live_mode: boolean;
  require_esc: boolean;
  card_number_length: number;
  security_code_length: number;
  first_six_digits: string;
  last_four_digits: string;
}

export interface CardholderDTO {
  name: string;
  identification: IdentificationDTO;
}

export interface IdentificationDTO {
  number: string;
  type: string;
}
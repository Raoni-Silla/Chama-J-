import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment.development';
import { MercadoPagoRequestDTO } from '../DTOS/MercadoPago/MercadoPagoRequestDTO.dto';
import { Observable, switchMap } from 'rxjs';
import { MercadoPagoResponseDTO } from '../DTOS/MercadoPago/MercadoPagoResponseDTO.dto';
import { CartaoRequestDTO } from '../DTOS/Cartao/CartaoRequestDTO.dto';
import { loadMercadoPago } from '@mercadopago/sdk-js';
import { from } from 'rxjs';
import { CartaoResponseDTO } from '../DTOS/Cartao/CartaoResponseDTO.dto';

@Injectable({
  providedIn: 'root',
})
export class CartaoService {
  private readonly apiUrl = 'http://localhost:8080/api/cartoes';

  private readonly mercadoPagoPromise = this.inicializarMercadoPago();

  constructor(private http: HttpClient) {}

  private async inicializarMercadoPago(): Promise<any> {
    await loadMercadoPago();

    if (!window.MercadoPago) {
      throw new Error('Não foi possível carregar o SDK do Mercado Pago');
    }

    return new window.MercadoPago(environment.mercadoPagoPublicKey);
  }

  criarTokenCartao(dadosCartao: MercadoPagoRequestDTO): Observable<MercadoPagoResponseDTO> {
    return from(this.mercadoPagoPromise).pipe(
      switchMap((mercadoPago) => {
        return from(
          mercadoPago.createCardToken({
            cardNumber: dadosCartao.card_number.replace(/\D/g, ''),

            cardholderName: dadosCartao.cardholder.name,

            cardExpirationMonth: dadosCartao.expiration_month,

            cardExpirationYear: dadosCartao.expiration_year,

            securityCode: dadosCartao.security_code,

            identificationType: dadosCartao.cardholder.identification.type,

            identificationNumber: dadosCartao.cardholder.identification.number.replace(/\D/g, ''),
          }) as Promise<MercadoPagoResponseDTO>,
        );
      }),
    );
  }

  processarESalvarCartao(dadosCartao: MercadoPagoRequestDTO): Observable<any> {
    return this.criarTokenCartao(dadosCartao).pipe(
      switchMap((mpResponse) => {
        const request: CartaoRequestDTO = {
          token: mpResponse.id,
        };

        return this.http.post(`${this.apiUrl}/criar`, request);
      }),
    );
  }

  listarCartoes(): Observable<CartaoResponseDTO[]> {
    return this.http.get<CartaoResponseDTO[]>(`${this.apiUrl}/listar`);
  }

  excluirCartao (id : number) : Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}

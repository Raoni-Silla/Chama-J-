import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { GeoapifyResponse } from '../DTOS/GeoApi/GeoapifyResponse.dto';
import { environment } from '../../environments/environment.development';

@Injectable({
  providedIn: 'root',
})
export class EnderecoService { 

  private apiUrl = 'http://localhost:8080/api/enderecos';

  constructor(private http: HttpClient) { }

  buscarEndereco(texto: string): Observable<GeoapifyResponse> {
    const url = `https://api.geoapify.com/v1/geocode/autocomplete?text=${texto}&apiKey=${environment.geoapifyKey}&lang=pt&limit=5&filter=countrycode:br`;
    return this.http.get<GeoapifyResponse>(url);
  }
}
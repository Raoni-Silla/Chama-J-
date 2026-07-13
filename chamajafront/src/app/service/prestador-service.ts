import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PrestadorResponseDTO } from '../DTOS/Prestador/PrestadorResponseDTO.dto';
import { MelhoresDoMesDTO } from '../DTOS/MelhoresDoMes/MelhoresDoMesDTO.dto';

@Injectable({
  providedIn: 'root',
})
export class PrestadorService {

  private apiUrl = 'http://localhost:8080/api/prestadores';

  constructor(private http: HttpClient) { }

  buscarPrestadores(termo: string): Observable<PrestadorResponseDTO[]> {
    let params = new HttpParams();
    if (termo) {
      params = params.set('q', termo);
    }
    return this.http.get<PrestadorResponseDTO[]>(`${this.apiUrl}/buscar`, { params });
  }

  buscarPrestadorPorId(id: number): Observable<PrestadorResponseDTO> {
    return this.http.get<PrestadorResponseDTO>(`${this.apiUrl}/${id}`);
  }

  obterTop5MelhoresPrestadores():Observable<MelhoresDoMesDTO[]>{
    return this.http.get<MelhoresDoMesDTO[]>(`${this.apiUrl}/top5`);
  }

}

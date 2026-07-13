import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UsuarioInfoBasicasDTO } from '../DTOS/Usuario/UsuarioInfoBasica.dto';
import { UsuarioInfoPerfilDTO } from '../DTOS/Usuario/UsuarioInfoPerfilDTO.dto';

@Injectable({
  providedIn: 'root',
})
export class UsuarioService {


  private apiUrl = 'http://localhost:8080/api/usuarios';

  constructor(private http: HttpClient) { }

  obterInfosBasicasUsuarioLogado(): Observable<UsuarioInfoBasicasDTO> {
    return this.http.get<UsuarioInfoBasicasDTO>(`${this.apiUrl}/obter-infos-basicas`);
  }

  obterInfosParaTelaDePerfil () : Observable<UsuarioInfoPerfilDTO>{
    return this.http.get<UsuarioInfoPerfilDTO>(`${this.apiUrl}/obter-infos-perfil`)
  }

}

import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { CadastroInicialRequest } from '../dto/Cadastro/CadastroInicialRequest.dto';
import { CadastroResponse } from '../dto/Cadastro/CadastroInicialResponse.dto';
import { Observable } from 'rxjs';
import { UsuarioResponse } from '../dto/Usuario/UsuarioResponse.dto';

@Injectable({
  providedIn: 'root',
})
export class RegisterService {

 private apiUrl = 'http://localhost:8080/registro'; 
  private http = inject(HttpClient);

  // POST: /iniciar (@RequestBody)
  iniciarCadastro(cadastroInicialRequest: CadastroInicialRequest): Observable<CadastroResponse> {
    return this.http.post<CadastroResponse>(`${this.apiUrl}/iniciar`, cadastroInicialRequest);
  }

  // POST: /telefone (@RequestParam)
  adicionarTelefone(id: number, telefone: string): Observable<CadastroResponse> {
    return this.http.post<CadastroResponse>(`${this.apiUrl}/telefone`, null, {
      params: { 
        id: id.toString(), 
        telefone: telefone 
      }
    });
  }

  // POST: /telefone/solicitarenvio (@RequestParam)
  solicitarEnvioSms(id: number): Observable<CadastroResponse> {
    return this.http.post<CadastroResponse>(`${this.apiUrl}/telefone/solicitarenvio`, null, {
      params: { 
        id: id.toString() 
      }
    });
  }

  // POST: /telefone/confirmarcodigo (@RequestParam)
  confirmarCodigoSms(id: number, codigoDigitadoPeloUsuario: string): Observable<CadastroResponse> {
    return this.http.post<CadastroResponse>(`${this.apiUrl}/telefone/confirmarcodigo`, null, {
      params: { 
        id: id.toString(), 
        codigoDigitadoPeloUsuario: codigoDigitadoPeloUsuario 
      }
    });
  }

  // POST: /tipousuario (@RequestParam)
  definirTipoUsuario(id: number, tipoUsuario: string): Observable<UsuarioResponse> {
    return this.http.post<UsuarioResponse>(`${this.apiUrl}/tipousuario`, null, {
      params: { 
        id: id.toString(), 
        tipoUsuario: tipoUsuario 
      }
    });
  }

}

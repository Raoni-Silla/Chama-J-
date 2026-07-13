import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Navbarlogged } from '../../../components/navbarlogged/navbarlogged';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UsuarioService } from '../../../service/usuario-service';
import { UsuarioInfoPerfilDTO } from '../../../DTOS/Usuario/UsuarioInfoPerfilDTO.dto';

@Component({
  selector: 'app-perfil-usuario',
  imports: [Navbarlogged, ReactiveFormsModule, CommonModule],
  templateUrl: './perfil-usuario.html',
  styleUrl: './perfil-usuario.css',
})
export class PerfilUsuario implements OnInit {
  perfilForm!: FormGroup;
  abaAtiva: string = 'perfil';
  dadosIniciais: UsuarioInfoPerfilDTO | null = null;
  urlFoto: string = '';

  constructor(private fb: FormBuilder, private cdr: ChangeDetectorRef, private usuarioService : UsuarioService) { }



  ngOnInit(): void {
    
    
    this.perfilForm = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      telefone: ['', Validators.required]
    });

    
    this.usuarioService.obterInfosParaTelaDePerfil().subscribe({
      next: (resposta) => {
        this.dadosIniciais = resposta;
        this.urlFoto = resposta.urlFoto;
      
        this.perfilForm.patchValue({
          nome: resposta.nome,
          email: resposta.email,
          telefone: resposta.telefone
        });

        this.cdr.detectChanges();
       
      },
      error: (err) => {
        console.error(err);
      }
    });
  }

  mudarAba(novaAba: string) {
    this.abaAtiva = novaAba;
  }

  salvarAlteracoes() {
    if (this.perfilForm.valid) {
      console.log('Dados prontos para enviar:', this.perfilForm.value);

      // AQUI ENTRARIA A SUA CHAMADA PARA O SERVICE (API)

      // Após salvar com sucesso, "limpamos" o estado de alterado
      // para o botão voltar a ficar desabilitado até a próxima edição
      this.perfilForm.markAsPristine();
    }
  }
}

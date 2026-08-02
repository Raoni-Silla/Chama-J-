import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Navbarlogged } from '../../../components/navbarlogged/navbarlogged';
import { FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UsuarioService } from '../../../service/usuario-service';
import { UsuarioInfoPerfilDTO } from '../../../DTOS/Usuario/UsuarioInfoPerfilDTO.dto';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { FileUploadModule } from 'primeng/fileupload';
import { ButtonModule } from 'primeng/button';
import { AbaPerfilPublico } from './components/aba-perfil-publico/aba-perfil-publico';
import { AbaConta } from './components/aba-conta/aba-conta';
import { AbaEnderecos } from './components/aba-enderecos/aba-enderecos';
import { AbaCartoes } from './components/aba-cartoes/aba-cartoes';

interface UploadEvent {
    originalEvent: Event;
    files: File[];
}

@Component({
  selector: 'app-perfil-usuario',
  imports: [Navbarlogged , ToastModule ,AbaPerfilPublico, AbaConta, AbaEnderecos, AbaCartoes],
   providers: [MessageService],
  templateUrl: './perfil-usuario.html',
  styleUrl: './perfil-usuario.css',
})
export class PerfilUsuario  {
  abaAtiva: string = 'perfil'
  mudarAba(novaAba: string) {
    this.abaAtiva = novaAba;
  }
}

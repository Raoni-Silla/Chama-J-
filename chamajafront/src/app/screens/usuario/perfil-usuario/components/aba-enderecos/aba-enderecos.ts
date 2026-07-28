import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormControl, ReactiveFormsModule, Validators } from '@angular/forms';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { InputTextModule } from 'primeng/inputtext';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { debounceTime, distinctUntilChanged, filter, switchMap, tap } from 'rxjs';
import { EnderecoService } from '../../../../../service/endereco-service';
import { GeoapifyFeature } from '../../../../../DTOS/GeoApi/GeoapifyFeature.dto';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { EnderecoRequestDTO } from '../../../../../DTOS/Endereco/EnderecoRequestDTO.dto';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
@Component({
  selector: 'app-aba-enderecos',
  imports: [CommonModule, IconFieldModule, InputIconModule, InputTextModule, ProgressSpinnerModule, ReactiveFormsModule, ToastModule, DialogModule, ButtonModule],
  providers: [MessageService],
  templateUrl: './aba-enderecos.html',
  styleUrl: './aba-enderecos.css',
})
export class AbaEnderecos implements OnInit {

  campoBusca = new FormControl('');
  sugestoes: GeoapifyFeature[] = [];
  enderecoSelecionado: GeoapifyFeature | null = null;
  numeroFormControl = new FormControl('', Validators.required);
  complementoFormControl = new FormControl('');
  mostrarModal = false;

  constructor(private enderecoService: EnderecoService, private cdr: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.campoBusca.valueChanges.pipe(
      debounceTime(200),
      tap(texto => {
        if (!texto || texto.length <= 2) {
            this.sugestoes = [];
            this.cdr.detectChanges(); 
        }
    }),
      filter(texto => (texto ?? '').length > 2),
      distinctUntilChanged(),
      switchMap(texto => this.enderecoService.buscarEndereco(texto as string))
    ).subscribe({
      next: (resultado) => {
        this.sugestoes = resultado.features;
        this.cdr.detectChanges();
      },
      error: (erro) => console.error('Erro ao buscar endereço', erro)
    });

  }

  selecionarEndereco(sugestao: GeoapifyFeature) {
    this.enderecoSelecionado = sugestao; 
    this.mostrarModal = true;
  }

  confirmarEEnviarEndereco() {
  if (!this.enderecoSelecionado || this.numeroFormControl.invalid) return;

  const dados = this.enderecoSelecionado.properties;

  const dto: EnderecoRequestDTO = {
    logradouro: dados.street || dados.formatted,
    numero: Number(this.numeroFormControl.value),
    complemento: this.complementoFormControl.value ?? undefined,
    nomeCidade: dados.city ?? '',
    siglaEstado: dados.state_code || 'SP', 
    cep: dados.postcode ?? '',
    latitude: dados.lat,
    longitude: dados.lon,
    enderecoPrincipal: true
  };

  console.log('DTO Pronto para o Spring Boot:', dto);
  
  // Aqui o this.enderecoService.salvar(dto).subscribe(...)

  this.mostrarModal = false; // Fecha o modal após enviar
}

}

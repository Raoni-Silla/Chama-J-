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
import { MenuItem, MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { EnderecoRequestDTO } from '../../../../../DTOS/Endereco/EnderecoRequestDTO.dto';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { EnderecoResponseDTO } from '../../../../../DTOS/Endereco/EnderecoResponseDTO.dto';
import { MenuModule } from 'primeng/menu';
@Component({
  selector: 'app-aba-enderecos',
  imports: [CommonModule, IconFieldModule, InputIconModule, InputTextModule, ProgressSpinnerModule, ReactiveFormsModule, ToastModule, DialogModule, ButtonModule, MenuModule],
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
  definirComoPrincipal = new FormControl(false);
  cepFormControl = new FormControl('', [Validators.required, Validators.minLength(8)]);
  mostrarModal = false;
  meusEnderecos: EnderecoResponseDTO[] | undefined = undefined;
  buscandoLocalizacao = false;
  items: MenuItem[] | undefined;
  enderecoClicado: EnderecoResponseDTO | null = null;
  modalAtualizarEndereco: boolean = false;
  carregando : boolean = false;

  constructor(private enderecoService: EnderecoService, private cdr: ChangeDetectorRef, private messageService: MessageService) { }

  ngOnInit(): void {


    this.items = [


      {
        label: 'Opções',
        items: [
          {
            label: 'Atualizar',
            icon: 'pi pi-refresh',
            command: () => {
              if (this.enderecoClicado && this.enderecoClicado.id !== undefined) {
                const id: number = this.enderecoClicado.id;
                console.log('Vai editar o ID:', id);
                this.atualizarEndereco(id);
              } else {
                console.warn('Nenhum endereço selecionado ou sem ID.');
              }
            }
          },
          {
            label: 'Excluir',
            icon: 'pi pi-trash',
            command: () => {
              if (this.enderecoClicado && this.enderecoClicado.id !== undefined) {
                const id: number = this.enderecoClicado.id;
                console.log('Vai excluir o ID:', id);
                this.excluirEndereco(id);
              } else {
                console.warn('Nenhum endereço selecionado ou sem ID.');
              }
            }
          }
        ]
      }
    ];


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


    this.obterEnderecos();



  }


  atualizarEndereco(id: number) {
    if (this.enderecoClicado) {


      this.cepFormControl.setValue(this.enderecoClicado.cep ?? '');


      this.numeroFormControl.setValue(this.enderecoClicado.numero?.toString() ?? '');

      this.complementoFormControl.setValue(this.enderecoClicado.complemento ?? '');
      this.definirComoPrincipal.setValue(this.enderecoClicado.enderecoPrincipal ?? false);


      this.cepFormControl.markAsPristine();
      this.numeroFormControl.markAsPristine();
      this.complementoFormControl.markAsPristine();
      this.definirComoPrincipal.markAsPristine();


      this.modalAtualizarEndereco = true;
    }
  }

  abrirMenuOpcoes(event: Event, menu: any, endereco: any) {
    this.enderecoClicado = endereco;
    menu.toggle(event);
  }

  obterLocalizacaoAtual() {
    if (!navigator.geolocation) {
      this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Seu navegador não suporta geolocalização.' });
      return;
    }

    this.buscandoLocalizacao = true;


    const opcoesGps = {
      enableHighAccuracy: true,
      timeout: 10000,
      maximumAge: 0
    };

    navigator.geolocation.getCurrentPosition(
      (position) => {
        const lat = position.coords.latitude;
        const lon = position.coords.longitude;

        console.log('📍 GPS Achou! Lat:', lat, 'Lon:', lon);

        this.enderecoService.buscarEnderecoPorCoordenadas(lat, lon).subscribe({
          next: (resultado) => {
            this.buscandoLocalizacao = false;

            if (resultado.features && resultado.features.length > 0) {
              this.selecionarEndereco(resultado.features[0]);
            } else {
              this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Não encontramos a rua exata.' });
            }
          },
          error: (err) => {
            this.buscandoLocalizacao = false;
            console.error('Erro na Geoapify:', err);
          }
        });
      },
      (error: GeolocationPositionError) => {
        this.buscandoLocalizacao = false;
        console.warn('❌ Erro no GPS do Navegador:', error.message, 'Código:', error.code);

        if (error.code === error.PERMISSION_DENIED) {
          this.messageService.add({ severity: 'error', summary: 'Permissão negada', detail: 'Você negou o acesso ao GPS.' });
          return;
        }

        if (error.code === error.POSITION_UNAVAILABLE || error.code === error.TIMEOUT) {

          this.messageService.add({
            severity: 'info',
            summary: 'Modo Desenvolvedor',
            detail: 'GPS físico indisponível. Usando localização simulada.'
          });

          const latFallback = -22.6617;
          const lonFallback = -50.4132;

          console.log('📍 Usando Fallback (Assis-SP): Lat:', latFallback, 'Lon:', lonFallback);

          this.enderecoService.buscarEnderecoPorCoordenadas(latFallback, lonFallback).subscribe({
            next: (resultado) => {
              if (resultado.features && resultado.features.length > 0) {
                this.selecionarEndereco(resultado.features[0]);
              }
            },
            error: (err) => console.error('Erro na Geoapify com o Fallback:', err)
          });
        }
      },
      opcoesGps
    );
  }

  selecionarEndereco(sugestao: GeoapifyFeature) {
    this.enderecoSelecionado = sugestao;
    const cepDaApi = sugestao.properties.postcode || '';
    this.cepFormControl.setValue(cepDaApi);
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
      cep: this.cepFormControl.value ?? '',
      latitude: dados.lat,
      longitude: dados.lon,
      enderecoPrincipal: this.definirComoPrincipal.value ?? false
    };

    console.log('DTO Pronto para o Spring Boot:', dto);

    this.enderecoService.salvarEndereco(dto).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Endereço Adicionado com Sucesso',
          detail: 'Parabéns, endereço cadastrado com sucesso',
          life: 3000
        });

        this.obterEnderecos();

        this.numeroFormControl.reset();
        this.complementoFormControl.reset('');
        this.campoBusca.reset('');
        this.sugestoes = [];
      },
      error: (err) => {
        console.error(err);
        this.messageService.add({
          severity: 'error',
          summary: 'Erro ao salvar endereço',
          detail: 'Não conseguimos salvar seu endereço, tente novamente',
          life: 3000
        });
      }
    })

    this.mostrarModal = false;
  }


  obterEnderecos() {
    this.carregando = true;
    this.enderecoService.obterEnderecos().subscribe({
      next: (resposta: EnderecoResponseDTO[]) => {
        this.meusEnderecos = resposta;
        this.carregando = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error(err);
        this.messageService.add({
          severity: 'error',
          summary: 'Erro ao obter seus enderços',
          detail: 'Não conseguimos obter seus endereço, tente novamente',
          life: 3000
        });
      }
    })
  }


  excluirEndereco(id: number) {

    if (!id || id === 0) {
      return;
    }

    this.enderecoService.excluirEndereco(id).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Endereço excluido com sucesso',
          detail: 'Endereço excluido corretamente',
          life: 3000
        })
        this.obterEnderecos();
      },
      error: (err) => {
        console.error(err);
        this.messageService.add({
          severity: 'error',
          summary: 'Erro ao excluir seus endereços',
          detail: 'Não conseguimos excluir seu endereço, tente novamente',
          life: 3000
        });
      }
    })

  }


 salvarEdicaoEndereco() {
    if (!this.enderecoClicado || this.numeroFormControl.invalid || this.cepFormControl.invalid) {
        return;
    }

    const dtoAtualizado: EnderecoRequestDTO = {
      logradouro: this.enderecoClicado.logradouro, 
      nomeCidade: this.enderecoClicado.nomeCidade, 
      siglaEstado: this.enderecoClicado.siglaEstado, 
      latitude: this.enderecoClicado.latitude, 
      longitude: this.enderecoClicado.longitude, 
      numero: Number(this.numeroFormControl.value),
      complemento: this.complementoFormControl.value ?? undefined,
      cep: this.cepFormControl.value ?? '',
      enderecoPrincipal: this.definirComoPrincipal.value ?? false
    };

    console.log('DTO de Atualização pronto:', dtoAtualizado);

   
    this.enderecoService.atualizarEndereco(this.enderecoClicado.id, dtoAtualizado).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Endereço Atualizado',
          detail: 'As alterações foram salvas com sucesso!',
          life: 3000
        });
        this.obterEnderecos();
        this.modalAtualizarEndereco = false;
      },
      error: (err) => {
        console.error('Erro ao atualizar:', err);
        this.messageService.add({
          severity: 'error',
          summary: 'Erro na atualização',
          detail: 'Não conseguimos atualizar o endereço, tente novamente.',
          life: 3000
        });
      }
    });
  }

}

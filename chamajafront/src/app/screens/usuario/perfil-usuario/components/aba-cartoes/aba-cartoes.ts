import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { CartaoService } from '../../../../../service/cartao-service';
import { DialogModule } from 'primeng/dialog';
import { FormsModule } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { CartaoRequestDTO } from '../../../../../DTOS/Cartao/CartaoRequestDTO.dto';
import { MercadoPagoRequestDTO } from '../../../../../DTOS/MercadoPago/MercadoPagoRequestDTO.dto';
import { NgxMaskDirective, provideNgxMask } from 'ngx-mask';
import { UsuarioService } from '../../../../../service/usuario-service';
import { switchMap } from 'rxjs';
import { CartaoResponseDTO } from '../../../../../DTOS/Cartao/CartaoResponseDTO.dto';
import { ProgressSpinnerModule } from 'primeng/progressspinner';

@Component({
  selector: 'app-aba-cartoes',
  imports: [
    CommonModule,
    ButtonModule,
    InputTextModule,
    DialogModule,
    FormsModule,
    ToastModule,
    NgxMaskDirective,
    ProgressSpinnerModule
  ],
  providers: [MessageService, provideNgxMask()],
  templateUrl: './aba-cartoes.html',
  styleUrl: './aba-cartoes.css',
})
export class AbaCartoes implements OnInit {
  meusCartoes: CartaoResponseDTO[] = [];
  cartaoSelecionado: CartaoResponseDTO | null = null;
  isModoEdicao: boolean = true;
  labelDinamico: string = 'Editar';
  isExibirModalAdicionarCartao: boolean = false;
  nomeTitular: string = '';
  numeroCartao: string = '';
  validade: string = '';
  codigoSeguranca: string = '';
  isSalvando: boolean = false;
  carregando : boolean = false;

  constructor(
    private cartaoService: CartaoService,
    private message: MessageService,
    private usuarioService: UsuarioService,
    private cdf: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {

    this.carregando = true;

    this.cartaoService.listarCartoes().subscribe({
      next: (res) => {
        console.log("lista de cartoes " + res)
        this.meusCartoes = res;
        this.carregando = false;
        this.cdf.detectChanges();
      },
      error: (err) => {
        console.error(err);
        this.message.add({
          severity: 'error',
          summary: 'Erro ao buscar cartões',
          detail: 'Impossivel encontrar seus cartões.',
          life: 3000,
        });
      },
    });
  }

  selecionarCartao(cartao: CartaoResponseDTO) {
    this.cartaoSelecionado = cartao;
    console.log('Cartão em destaque alterado para:', cartao.bandeira);
  }

  excluirCartao(id: number) {
    this.cartaoService.excluirCartao(id).subscribe({
      next: () => {
        this.message.add({
          severity: 'success',
          summary: 'Dados apagados com sucesso',
          detail: 'Cartão excluido com sucesso do nosso sistema.',
          life: 3000,
        });
        this.ngOnInit();
      },
      error: () => {
        this.message.add({
          severity: 'error',
          summary: 'Erro ao processar a exclusão',
          detail: 'Por favor, tente novamente',
          life: 3000,
        });
      },
    });
  }

  salvarDadosAlterados() {}

  autorizarEdicao() {
    this.isModoEdicao = true;
  }

  voltarParaModoVisualizacao() {
    this.isModoEdicao = false;
  }

  adicionarNovoCartao(): void {
    console.log('chegou aqui antes de validar mes e ano');

    if (!this.validarMesAno(this.validade)) {
      console.log(this.validade);
      console.log('morreu aqui ');
      this.message.add({
        severity: 'error',
        summary: 'Insira uma data válida',
        detail: 'Por favor, insira uma data de expiração válida.',
        life: 3000,
      });

      return;
    }

    this.isSalvando = true;

    this.usuarioService
      .obterCpf()
      .pipe(
        switchMap((res) => {
          console.log('obteu cpf');

          const [mes, anoAbreviado] = this.validade.split('/');
          const anoCompleto = `20${anoAbreviado}`;

          const dto: MercadoPagoRequestDTO = {
            card_number: this.numeroCartao,
            security_code: this.codigoSeguranca,
            expiration_month: mes,
            expiration_year: anoCompleto,
            cardholder: {
              name: this.nomeTitular,
              identification: {
                type: 'CPF',
                number: res.cpf,
              },
            },
          };

          return this.cartaoService.processarESalvarCartao(dto);
        }),
      )
      .subscribe({
        next: () => {
          this.message.add({
            severity: 'success',
            summary: 'Cartão salvo',
            detail: 'Seu cartão foi validado e salvo com sucesso.',
            life: 3000,
          });
          this.isExibirModalAdicionarCartao = false;
          this.isSalvando = false;
          this.nomeTitular = ''
          this.numeroCartao = ''
          this.validade = ''
          this.codigoSeguranca = ''
          this.ngOnInit();
        },
        error: (erro) => {
          console.error('Erro ao salvar cartão:', erro);

          this.message.add({
            severity: 'error',
            summary: 'Erro ao salvar cartão',
            detail: 'Não foi possível validar ou salvar o cartão.',
            life: 3000,
          });

          this.isSalvando = false;
        },
      });
  }

  validarMesAno(input: string): boolean {
    const regex = /^(0[1-9]|1[0-2])\/\d{2}$/;

    if (!regex.test(input)) {
      return false;
    }

    const [mes, anoAbreviado] = input.split('/').map(Number);

    const anoCompleto = 2000 + anoAbreviado;

    const dataInput = new Date(anoCompleto, mes - 1, 1);

    const hoje = new Date();
    const dataAtual = new Date(hoje.getFullYear(), hoje.getMonth(), 1);

    return dataInput >= dataAtual;
  }
}

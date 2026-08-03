import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { FileUploadModule } from 'primeng/fileupload';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { ToastModule } from 'primeng/toast';
import { UsuarioService } from '../../../../../service/usuario-service';
import { DialogModule } from 'primeng/dialog';
import { LoginService } from '../../../../../service/login-service';
import { Router, RouterLink, RouterModule } from '@angular/router';

@Component({
  selector: 'app-aba-conta',
  imports: [
    CommonModule,
    ProgressSpinnerModule,
    ToastModule,
    InputGroupModule,
    InputGroupAddonModule,
    FormsModule,
    FileUploadModule,
    ButtonModule,
    ReactiveFormsModule,
    DialogModule,
    RouterModule,
  ],
  providers: [MessageService],
  templateUrl: './aba-conta.html',
  styleUrl: './aba-conta.css',
})
export class AbaConta implements OnInit {
  exibirTrocaSenha = false;
  mostrarSenhaAtual = false;
  senhaForm = new FormGroup({
    senhaAtual: new FormControl('', [Validators.required]),
    novaSenha: new FormControl('', [
      Validators.required,
      Validators.minLength(8),
      Validators.pattern(
        /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&#_-])[A-Za-z\d@$!%*?&#_-]{8,}$/,
      ),
    ]),
    confirmarSenha: new FormControl('', [Validators.required]),
  });
  cpf = '';
  carregando: boolean = false;
  abrirModalDesativar: boolean = false;
  abrirModalExcluir: boolean = false;
  desativar: string = '';
  confirmacaoExcluir: string = '';
  excluindoConta: boolean = false;

  constructor(
    private fb: FormBuilder,
    private cdr: ChangeDetectorRef,
    private messageService: MessageService,
    private usuarioService: UsuarioService,
    private loginService: LoginService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.carregando = true;
    this.usuarioService.obterInfosParaTelaDePerfil().subscribe({
      next: (res) => {
        this.cpf = res.cpf;
        this.carregando = false;
        this.cdr.detectChanges();
      },
    });
  }

  atualizarSenha() {
    if (this.senhaForm.valid) {
      const valoresDoFormulario = this.senhaForm.value;
      console.log('Dados prontos para enviar para a API:', valoresDoFormulario);

      this.usuarioService
        .trocarSenha(
          this.senhaForm.get('senhaAtual')?.value ?? '',
          this.senhaForm.get('novaSenha')?.value ?? '',
        )
        .subscribe({
          next: () => {
            this.messageService.add({
              severity: 'success',
              summary: 'Senha Atualizada',
              detail: 'Concluimos com sucesso a troca de senha',
              life: 3000,
            });
            this.senhaForm.reset();
            this.exibirTrocaSenha = false;
          },
          error: (err) => {
            console.error(err);
            this.senhaForm.reset();
            this.exibirTrocaSenha = false;
            this.messageService.add({
              severity: 'error',
              summary: 'Senha Não Atualizada',
              detail: 'Não Podemos Concluir a Troca de Senha',
              life: 3000,
            });
          },
        });
    } else {
      console.log('Formulário inválido. Preencha os campos corretamente.');
    }
  }

  get valorSenha(): string {
    return this.senhaForm.get('novaSenha')?.value || '';
  }

  get temTamanho() {
    return this.valorSenha.length >= 8;
  }
  get temMaiuscula() {
    return /[A-Z]/.test(this.valorSenha);
  }
  get temMinuscula() {
    return /[a-z]/.test(this.valorSenha);
  }
  get temNumero() {
    return /\d/.test(this.valorSenha);
  }
  get temEspecial() {
    return /[@$!%*?&#_-]/.test(this.valorSenha);
  }

  get senhasBatem() {
    return (
      (this.senhaForm.get('novaSenha')?.value ?? '') ===
      (this.senhaForm.get('confirmarSenha')?.value ?? '')
    );
  }

  onUpload(arquivo: any) {}

  get confirmacaoDesativacaoValida(): boolean {
    return this.desativar.trim().toLowerCase() === 'desativar';
  }

  fecharModalDesativar(): void {
    this.abrirModalDesativar = false;
    this.desativar = '';
  }

  confirmarDesativacao(): void {
    if (!this.confirmacaoDesativacaoValida) {
      return;
    }

    this.usuarioService.desativarConta().subscribe({
      next: () => {
        console.log('deu bom');
        this.loginService.limparToken();
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error(err);
      },
    });
  }

  get confirmacaoExclusaoValida(): boolean {
    return this.confirmacaoExcluir.trim().toLowerCase() === 'excluir';
  }

  limparConfirmacaoExclusao(): void {
    this.confirmacaoExcluir = '';
    this.excluindoConta = false;
  }

  fecharModalExcluir(): void {
    this.abrirModalExcluir = false;
    this.limparConfirmacaoExclusao();
  }

  excluirConta(): void {
    if (!this.confirmacaoExclusaoValida || this.excluindoConta) {
      return;
    }

    this.excluindoConta = true;

    this.usuarioService.excluirConta().subscribe({
      next: () => {
        localStorage.removeItem('token');

        this.abrirModalExcluir = false;
        this.confirmacaoExcluir = '';
        this.excluindoConta = false;
        this.loginService.limparToken();

        this.router.navigate(['/login']);
      },
      error: (erro) => {
        console.error('Erro ao excluir conta:', erro);

        this.excluindoConta = false;
      },
    });
  }
}

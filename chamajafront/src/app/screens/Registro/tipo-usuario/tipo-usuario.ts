import { Component, OnInit } from '@angular/core';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { LoginService } from '../../../service/login-service';
import { Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-tipo-usuario',
  imports: [ToastModule, ButtonModule],
  providers: [MessageService],
  templateUrl: './tipo-usuario.html',
  styleUrl: './tipo-usuario.css',
})
export class TipoUsuario implements OnInit {
  
  constructor(private messageService: MessageService, private loginService: LoginService, private router: Router){}

  ngOnInit(): void {
    const token = this.loginService.obterToken();
    if (!token) {
      this.router.navigate(['/register']);
    }
  }

  prestador (){
    this.loginService.tipoUsuario("PRESTADOR").subscribe({
      next:(response)=>{
        this.messageService.add({
          severity:"success",
          summary: 'Aproveite a plataforma',
          detail:'Usuario criado com sucesso',
          life: 3000
        });
        
        this.loginService.limparToken(); 
        this.loginService.salvarToken(response.tokenDefinitivo);
        
        console.log(response);
        this.router.navigate(["/homepageprestador"]);
      },
      error:(err)=> console.error(err)
    })
  }

  usuario (){
    this.loginService.tipoUsuario("USUARIO").subscribe({
      next:(response)=>{
        this.messageService.add({
          severity:"success",
          summary: 'Aproveite a plataforma',
          detail:'Usuario criado com sucesso',
          life: 3000
        });

        this.loginService.limparToken();
        this.loginService.salvarToken(response.tokenDefinitivo);

        console.log(response);
        this.router.navigate(["/homepageuser"]);
      },
      error:(err)=> console.error(err)
    })
  }
}
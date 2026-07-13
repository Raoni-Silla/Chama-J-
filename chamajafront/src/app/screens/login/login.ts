import { Component } from '@angular/core';
import { Defaultbutton } from '../../components/defaultbutton/defaultbutton';
import { FormsModule } from '@angular/forms';
import { LoginService } from '../../service/login-service';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { Router, RouterModule } from '@angular/router';
import { jwtDecode } from 'jwt-decode';

@Component({
  selector: 'app-login',
  imports: [Defaultbutton, FormsModule,ToastModule,RouterModule],
  providers: [MessageService],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {

  email : string = '';
  password : string = '';

  constructor(private loginService: LoginService, private messageService: MessageService, private router: Router) {}

autenticar() {
  this.loginService.autenticar(this.email, this.password).subscribe({
    next: (response) => {

      const token = response.token;

      this.loginService.salvarToken(token);

      const payloadAberto: any = jwtDecode(token);

      console.log(payloadAberto);

      const role = payloadAberto.ROLE; 

      if (role === 'PRESTADOR') {
        this.router.navigate(['/homepageprestador']);
      } 
      else if (role === 'USUARIO') {
        this.router.navigate(['/homepageuser']);
      }
      else if (role === 'ADMIN') {
        this.router.navigate(['/admin']);
      }

    },
    error: (error) => {
      console.error('Erro na autenticação:', error);

      this.messageService.add({
        severity: 'error',
        summary: 'Erro na autenticação',
        detail: 'Verifique seu email e senha e tente novamente.',
        life: 3000
      });
    }
  });
}


}

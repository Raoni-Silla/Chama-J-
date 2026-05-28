import { Component, OnDestroy, OnInit } from '@angular/core';
import { Defaultbutton } from '../../components/defaultbutton/defaultbutton';
import { FormBuilder, FormGroup, FormsModule, NgModel, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register',
  imports: [Defaultbutton],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register implements OnInit, OnDestroy {

  ngOnDestroy(): void {
    console.log("oi")
  }
  ngOnInit(): void {
    console.log("oi")
  }

  nome : string = '';
  cpf : string = '';
  dataNascimento : string = '';
  email : string = '';
  senha : string = '';


  


}

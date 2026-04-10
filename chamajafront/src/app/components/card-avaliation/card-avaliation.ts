import { Component, input } from '@angular/core';

@Component({
  selector: 'app-card-avaliation',
  imports: [],
  templateUrl: './card-avaliation.html',
  styleUrl: './card-avaliation.css',
})
export class CardAvaliation {
  avaliation =  input<string>(''); 
  nameUser =  input<string>(''); 
  userType = input<string>('');
  imageSrc = input<string>('');
}

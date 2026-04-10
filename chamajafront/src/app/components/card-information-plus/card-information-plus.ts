import { Component, input } from '@angular/core';

@Component({
  selector: 'app-card-information-plus',
  imports: [],
  templateUrl: './card-information-plus.html',
  styleUrl: './card-information-plus.css',
})
export class CardInformationPlus {
  imageSrc = input<string>(''); // Para URLs de imagens externas ou locais
  label = input<string>('Pagamento Seguro');
  describe = input<string>('hello,world');
}

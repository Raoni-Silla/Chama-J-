import { Component, input, Input } from '@angular/core';

@Component({
  selector: 'app-card-information-mini',
  imports: [],
  templateUrl: './card-information-mini.html',
  styleUrl: './card-information-mini.css',
})
export class CardInformationMini {
  imageSrc = input<string>(''); // Para URLs de imagens externas ou locais
  label = input<string>('Pagamento Seguro');
}

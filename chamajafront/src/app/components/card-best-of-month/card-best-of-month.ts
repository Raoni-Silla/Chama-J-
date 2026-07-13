import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-card-best-of-month',
  imports: [],
  templateUrl: './card-best-of-month.html',
  styleUrl: './card-best-of-month.css',
})
export class CardBestOfMonth {
  @Input() id : number = 0;
  @Input() nome: string = 'raoni';
  @Input() urlFoto: string = 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQVIPMX5Zny3H110_y2XDbDD-_3yHuAnze4S-qVTcsiSFPrIFMe_OjsuAg&s=10';
  @Input() categorias: string[] = [];
  @Input() notaMedia: number = 5.0;
  @Input() isVerificado: boolean = false;
  @Input() quantidadeAvaliacoes: number = 127;
  @Output() saberMais = new EventEmitter<number>();

  clicarSaberMais() {
    this.saberMais.emit(this.id);
  }
}

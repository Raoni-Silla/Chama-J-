import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-floatingcard',
  imports: [CommonModule],
  templateUrl: './floatingcard.html',
  styleUrl: './floatingcard.css',
})
export class Floatingcard {
  @Input() title : string = '';
  @Input() subtitle: string =''; 
}

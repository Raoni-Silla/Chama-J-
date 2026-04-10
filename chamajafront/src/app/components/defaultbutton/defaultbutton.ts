import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-defaultbutton',
  imports: [CommonModule],
  templateUrl: './defaultbutton.html',
  styleUrl: './defaultbutton.css',
})
export class Defaultbutton {
  @Input() type: string = '';
}

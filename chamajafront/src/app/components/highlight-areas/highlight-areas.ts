import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-highlight-areas',
  imports: [FormsModule, CommonModule],
  templateUrl: './highlight-areas.html',
  styleUrl: './highlight-areas.css',
})
export class HighlightAreas {

  @Input() area : string = 'technology';
  @Input() icon : string = 'pi-home'

}
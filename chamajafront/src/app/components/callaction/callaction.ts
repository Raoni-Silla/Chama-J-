import { Component } from '@angular/core';
import { Defaultbutton } from '../defaultbutton/defaultbutton';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-callaction',
  imports: [Defaultbutton, RouterLink],
  templateUrl: './callaction.html',
  styleUrl: './callaction.css',
})
export class Callaction {}

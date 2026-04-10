import { Component } from '@angular/core';
import { Defaultbutton } from '../defaultbutton/defaultbutton';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-navbar',
  imports: [Defaultbutton, RouterLink],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class Navbar {}

import { Component } from '@angular/core';
import { Routes } from '@angular/router';
import { Landingpage } from './screens/landingpage/landingpage';
import { Login } from './screens/login/login';
import { Register } from './screens/register/register';
import { RegisterTelefone } from './screens/register-telefone/register-telefone';
import { RegisterClientePrestador } from './screens/register-cliente-prestador/register-cliente-prestador';

export const routes: Routes = [    
 { path: '', component: Landingpage },
 { path: 'login', component: Login},
 { path:'register', component: Register},
 { path: 'register-telefone', component: RegisterTelefone},
 { path: 'register-cliente-prestador', component: RegisterClientePrestador}
];

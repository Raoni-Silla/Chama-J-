import { Component } from '@angular/core';
import { Routes } from '@angular/router';
import { Landingpage } from './screens/landingpage/landingpage';
import { Login } from './screens/login/login';
import { Register } from './screens/register/register';

export const routes: Routes = [    
 { path: '', component: Landingpage },
 { path: 'login', component: Login},
 { path:'register', component: Register}
];

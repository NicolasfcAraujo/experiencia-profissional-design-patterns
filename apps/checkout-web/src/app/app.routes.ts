import { Routes } from '@angular/router';
import { Checkout } from './pages/checkout/checkout';
import { Historico } from './pages/historico/historico';

export const routes: Routes = [
  { path: '', redirectTo: 'checkout', pathMatch: 'full' },
  { path: 'checkout', component: Checkout },
  { path: 'historico', component: Historico },
  { path: '**', redirectTo: 'checkout' }
];

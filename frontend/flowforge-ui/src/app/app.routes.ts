import { Routes } from '@angular/router';
import { MainLayout } from './core/layout/main-layout/main-layout';
import { Dashboard } from './features/dashboard/pages/dashboard/dashboard';
import { Login } from './features/auth/pages/login/login';
import { authGuard } from './core/guards/auth-guard';

export const routes: Routes = [
  {
  path: 'auth/login',
  component: Login
  },
    {
    path:'',
    component: MainLayout,
    children: [
      {
        path:'dashboard',
        canActivate: [authGuard],
        component: Dashboard
      },    
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      }
    ]
  },

  {
    path:'**',
    redirectTo: 'auth/login'
  }
];

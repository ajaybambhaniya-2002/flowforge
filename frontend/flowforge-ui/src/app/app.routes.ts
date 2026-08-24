import { Routes } from '@angular/router';
import { MainLayout } from './core/layout/main-layout/main-layout';
import { Dashboard } from './features/dashboard/pages/dashboard/dashboard';
import { Login } from './features/auth/pages/login/login';
import { authGuard } from './core/guards/auth-guard';
import { ForgotPassword } from './features/auth/pages/forgot-password/forgot-password';
import { ResetPassword } from './features/auth/pages/reset-password/reset-password';
import { RegistrationForm } from './features/auth/pages/registration-form/registration-form';

export const routes: Routes = [
  {
  path: 'auth/login',
  component: Login
  },
    {
    path:'',
    component: MainLayout,
    canActivate: [authGuard],
    children: [
       {
      path: 'dashboard',
      loadComponent: () =>
        import('./features/dashboard/pages/dashboard/dashboard')
          .then(m => m.Dashboard)
      },    
       {
      path: 'profile',
      loadComponent: () =>
        import('./features/profile/pages/profile/profile')
          .then(m => m.Profile)
      },    
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      }
    ]
  },
  {
     path: 'auth/forgot-password',
     component:ForgotPassword
  },
  {
     path: 'auth/register',
     component:RegistrationForm
  },
  {
     path: 'auth/reset-password',
     component:ResetPassword
  },
  {
    path:'**',
    redirectTo: 'auth/login'
  }
];

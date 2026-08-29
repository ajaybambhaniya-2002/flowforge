import { ApplicationConfig, inject, provideAppInitializer, provideBrowserGlobalErrorListeners, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { authInterceptor } from './core/interceptors/auth-interceptor';
import { SessionService } from './core/services/session-service';
import { firstValueFrom } from 'rxjs';
import { provideCharts, withDefaultRegisterables } from 'ng2-charts';
export const appConfig: ApplicationConfig = {
  providers: [
    provideAppInitializer(() => {
      // Application starts
      // ↓
      // Session restoration
      //       ↓
      // wait
      //       ↓
      // /auth/refresh
      //       ↓
      // complete
      //       ↓
      // Router starts
      //       ↓
      // Route Guard
      const sessionService = inject(SessionService);

      return firstValueFrom(
        sessionService.restoreSession()
      );

    }),
    provideBrowserGlobalErrorListeners(),
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideCharts(withDefaultRegisterables()),
    provideRouter(routes),
    provideHttpClient(
       withInterceptors([
        authInterceptor
      ])
    )
  ]
};

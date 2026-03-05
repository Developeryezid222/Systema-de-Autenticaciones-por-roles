import { ApplicationConfig, inject } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideAnimations } from '@angular/platform-browser/animations'; 
import { provideHttpClient, withInterceptors  } from '@angular/common/http';
import { providePrimeNG } from 'primeng/config';
import Aura from '@primeuix/themes/aura';

import { ProdInterceptorService } from './interceptors/prod-interceptor.service';
import { TokenService } from './security/token.service';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideAnimations(),
    provideHttpClient(
      withInterceptors([
    (req, next) => {
      const tokenService = inject(TokenService);
      const token = tokenService.getToken();
      if (token) {
        const cloned = req.clone({
          setHeaders: { Authorization: `Bearer ${token}` }
        });
        return next(cloned);
      }
      return next(req);
    }
  ])
    ),
    providePrimeNG({
      theme: {
        preset: Aura,
        options: {
            darkModeSelector: '.my-app-dark'
        }
      }
    })]
};

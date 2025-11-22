import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';

import { provideAnimations } from '@angular/platform-browser/animations';
import { provideToastr } from 'ngx-toastr';

import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';

// IMPORT DO SEU INTERCEPTOR AQUI
import { authInterceptor } from './interceptors/auth.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideAnimations(),
    provideToastr(),
    provideHttpClient(
      withFetch(),                       // mantém fetch
      withInterceptors([authInterceptor]) // registra o interceptor
    )
  ]
};

import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { routes } from './app.routes';
import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from './services/auth.service';
import { environment } from 'src/environments/environment';

// Prepends environment.apiBaseUrl to any bare /api/* request so components
// using `/api/...` paths work whether deployed as a single service (same
// origin) or split across two Render services (backend URL prefixed).
// Skipped when apiBaseUrl is empty (single-service / dev-proxy mode).
const apiPrefixInterceptor: HttpInterceptorFn = (req, next) => {
  if (environment.apiBaseUrl && req.url.startsWith('/api')) {
    req = req.clone({ url: environment.apiBaseUrl + req.url });
  }
  return next(req);
};

const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken();
  if (token) {
    req = req.clone({ setHeaders: { Authorization: `Bearer ${token}` } });
  }
  return next(req);
};

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    // Order matters: prefix the URL first, then attach the auth header.
    provideHttpClient(withInterceptors([apiPrefixInterceptor, jwtInterceptor]))
  ]
};

import {inject} from '@angular/core';
import {CanActivateFn, Router} from '@angular/router';
import {TokenService} from '../security/token.service';

export const authGuard: CanActivateFn = (route, state) => {
  const tokenService = inject(TokenService);
  const router = inject(Router);

  console.log('AuthGuard: Verificando acceso a', state.url);

  const token = tokenService.getToken();
  if (token) {
    console.log('AuthGuard: Acceso concedido - Token válido');
    return true; // Permite la navegación
  } else {
    console.log('AuthGuard: Acceso denegado - Sin token, redirigiendo a /login');
    // Limpiar cualquier dato residual
    tokenService.logOut();
    router.navigate(['/login']); // Redirige a login
    return false; // Bloquea la navegación
  }
}
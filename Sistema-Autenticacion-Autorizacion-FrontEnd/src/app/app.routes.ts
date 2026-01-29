import { Routes } from '@angular/router';

import { LoginComponent } from './auth/login.component';
import { IndexComponent } from './index/index.component';
import { authGuard } from './guards/auth.guard';

import { GestionUsuarioComponent } from './features/gestion-usuario/gestion-usuario.component';
import { ForgotpasswordComponent } from './forgotpassword/forgotpassword.component';

export const routes: Routes = [
    { path: '', redirectTo: '/home', pathMatch: 'full' }, // Redirige a home por defecto
    { path: 'login', component: LoginComponent },
    { path: 'home', component: IndexComponent, canActivate: [authGuard] }, // Home protegido
    { path: 'verGestionUsuarios', component: GestionUsuarioComponent, canActivate: [authGuard] }, // Gestión de usuarios protegida
    //{ path: '**', redirectTo: '/home', pathMatch: 'full' }, // Cualquier ruta no válida va a home
    { path: 'forgot-password', component: ForgotpasswordComponent} // Nueva ruta para "Forgot Password"
];

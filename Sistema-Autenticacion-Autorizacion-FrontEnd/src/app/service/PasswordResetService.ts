import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AppSettings } from '../app.settings';

const BASE_URL = AppSettings.API_ENDPOINT + '/api/password';

@Injectable({
  providedIn: 'root'
})
export class PasswordResetService {

  constructor(private http: HttpClient) {}

  forgotPassword(correo: string): Observable<{ mensaje: string }> {
    return this.http.post<{ mensaje: string }>(`${BASE_URL}/forgot`, { correo });
  }

  validarToken(token: string): Observable<{ valido: boolean }> {
    return this.http.get<{ valido: boolean }>(`${BASE_URL}/validar`, { params: { token } });
  }

  resetPassword(payload: {
    token: string;
    newPassword: string;
    confirmPassword: string;
  }): Observable<{ mensaje: string }> {
    return this.http.post<{ mensaje: string }>(`${BASE_URL}/reset`, payload);
  }
}
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AppSettings } from '../app.settings';

const BASE_URL = AppSettings.API_ENDPOINT + '/password';

@Injectable({
  providedIn: 'root'
})
export class PasswordResetService {

  constructor(private http: HttpClient) {}

  forgotPassword(email: string): Observable<{ mensaje: string }> {
    return this.http.post<{ mensaje: string }>(`${BASE_URL}/forgot`, { email });
  }

  validarToken(token: string): Observable<{ valido: boolean }> {
    return this.http.get<{ valido: boolean }>(`${BASE_URL}/validar`, { params: { token } });
  }

  resetPassword(payload: {
    token: string;
    nuevaPassword: string;
    confirmarPassword: string;
  }): Observable<{ mensaje: string }> {
    return this.http.post<{ mensaje: string }>(`${BASE_URL}/reset`, payload);
  }
}
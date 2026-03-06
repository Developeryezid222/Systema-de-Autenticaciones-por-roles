import { Component, OnInit } from '@angular/core';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './forgotpassword.component.html',
  styleUrl: './forgotpassword.component.scss'
})
export class ForgotpasswordComponent implements OnInit {

  // ──────────────────────────────────────────
  // Estado del flujo
  // 'email'  → Paso 1: ingresar correo
  // 'reset'  → Paso 3: ingresar nueva contraseña (token ya validado)
  // 'done'   → Contraseña cambiada exitosamente
  // ──────────────────────────────────────────
  step: 'email' | 'reset' | 'done' | 'tokenInvalido' = 'email';

  hideNewPassword = true;
  hideConfirmPassword = true;
  isLoading = false;
  mensajeError = '';
  mensajeExito = '';

  token = '';

  private readonly API = 'http://localhost:8090/api/password';

  // ── Paso 1: formulario de correo ──────────
  emailForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email])
  });

  // ── Paso 3: formulario de nueva contraseña ─
  resetForm = new FormGroup({
    newPassword: new FormControl('', [
      Validators.required,
      Validators.minLength(8),
      Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]+$')
    ]),
    confirmPassword: new FormControl('', Validators.required)
  }, {
    validators: (group) => this.passwordMatchValidator(group)
  });

  constructor(
    private http: HttpClient,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Si la URL trae ?token=xxx, validamos el token y vamos al paso de reset
    this.route.queryParams.subscribe(params => {
      const tokenParam = params['token'];
      if (tokenParam) {
        this.token = tokenParam;
        this.validarToken(tokenParam);
      }
    });
  }

  // ── Paso 1: solicitar correo ──────────────
  onSubmitEmail(): void {
    if (this.emailForm.invalid) {
      this.emailForm.markAllAsTouched();
      return;
    }

    this.isLoading = true;
    this.mensajeError = '';
    this.mensajeExito = '';

    const email = this.emailForm.value.email;

    this.http.post<{ mensaje2: string }>(`${this.API}/forgot`, { email }).subscribe({
      next: (res) => {
        this.isLoading = false;
        this.mensajeExito = res.mensaje2 ??
          'Si el correo está registrado, recibirás un enlace en breve.';
      },
      error: (err) => {
        this.isLoading = false;
        this.mensajeError = err.error?.mensaje1 ?? 'Ocurrió un error. Intenta nuevamente.';
      }
    });
  }

  // ── Paso 2: validar token (llamado desde ngOnInit) ──
  private validarToken(token: string): void {
    this.isLoading = true;
    this.mensajeError = '';

    this.http.get<{ valido: boolean; mensaje1?: string }>(
      `${this.API}/validar`, { params: { token } }
    ).subscribe({
      next: (res) => {
        this.isLoading = false;
        if (res.valido) {
          this.step = 'reset';
        } else {
          this.mensajeError = res.mensaje1 ?? 'El enlace ha expirado o ya fue utilizado.';
        }
      },
      error: (err) => {
        this.isLoading = false;
        this.mensajeError = err.error?.mensaje1 ?? 'El enlace ha expirado o ya fue utilizado.';
      }
    });
  }

  // ── Paso 3: cambiar contraseña ────────────
  onSubmitReset(): void {
    if (this.resetForm.invalid) {
      this.resetForm.markAllAsTouched();
      return;
    }

    this.isLoading = true;
    this.mensajeError = '';

    const body = {
      token: this.token,
      newPassword: this.resetForm.value.newPassword
    };

    this.http.post<{ mensaje: string }>(`${this.API}/reset`, body).subscribe({
      next: () => {
        this.isLoading = false;
        this.step = 'done';
      },
      error: (err) => {
        this.isLoading = false;
        this.mensajeError = err.error?.mensaje ?? 'No se pudo actualizar la contraseña.';
      }
    });
  }

  irAlLogin(): void {
    this.router.navigate(['/login']);
  }

  // ── Validador: contraseñas coinciden ──────
  passwordMatchValidator(group: AbstractControl): { [key: string]: any } | null {
    const newPass = group.get('newPassword')?.value;
    const confirmPass = group.get('confirmPassword')?.value;
    return newPass === confirmPass ? null : { notMatching: true };
  }

  get emailControls() { return this.emailForm.controls; }
  get resetControls() { return this.resetForm.controls; }
}
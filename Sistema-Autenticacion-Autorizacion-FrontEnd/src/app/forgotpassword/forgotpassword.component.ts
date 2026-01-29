import { Component } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-forgotpassword',
  imports: [CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule],
  templateUrl: './forgotpassword.component.html',
  styleUrl: './forgotpassword.component.scss'
})
export class ForgotpasswordComponent {
  
  
  hideNewPassword = true;
  hideConfirmPassword = true;

  
  forgotPasswordForm = new FormGroup({
    newPassword: new FormControl('', [
      Validators.required,
      Validators.minLength(8),
      Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]+$')
    ]),
    confirmPassword: new FormControl('', Validators.required)
  }, {
    validators: (group) => this.passwordMatchValidator(group)
  });


    constructor() { }

  // Validador personalizado
  passwordMatchValidator(group: AbstractControl): { [key: string]: any } | null {
    const newPass = group.get('newPassword')?.value;
    const confirmPass = group.get('confirmPassword')?.value;
    return newPass === confirmPass ? null : { notMatching: true };
  }

  onSubmit() {
    if (this.forgotPasswordForm.valid) {
      console.log('Contraseña restablecida:', this.forgotPasswordForm.value.newPassword);
      // Aquí iría la lógica para llamar al servicio de restablecimiento de contraseña
    } else {
      // Marcar todos los campos como "tocados" para mostrar los errores de validación
      this.forgotPasswordForm.markAllAsTouched();
    }
  }

  // Método auxiliar para acceder a los controles del formulario fácilmente en la plantilla
  get formControls() {
    return this.forgotPasswordForm.controls;
  }
}

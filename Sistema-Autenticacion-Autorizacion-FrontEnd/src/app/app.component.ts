import { Component } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { MenuComponent } from './menu/menu.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet,MenuComponent,CommonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'CiberPass-Frontend';
  constructor(private router: Router) {
    console.log('Ruta actual:', this.router.url);
  }
  isLoginPage(): boolean {
    return this.router.url === '/login'
  }
}

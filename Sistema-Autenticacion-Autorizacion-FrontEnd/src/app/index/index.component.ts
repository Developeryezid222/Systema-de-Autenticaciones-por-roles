import { Component, OnInit } from '@angular/core';
import { TokenService } from '../security/token.service';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';

interface SystemStats {
  activeUsers: string;
  totalUsers: string;
  securityLevel: string;
}

@Component({
  selector: 'app-index',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './index.component.html',
  styleUrls: ['./index.component.css']
})
export class IndexComponent implements OnInit {
  isLogged = false;
  currentYear: number = new Date().getFullYear();
  systemStats: SystemStats = {
    activeUsers: '42',
    totalUsers: '145',
    securityLevel: 'Alto'
  };

  private nombreUsuario = "";

  constructor(private tokenService: TokenService) {}

  ngOnInit() {
    if (this.tokenService.getToken()) {
      this.isLogged = true;
    } else {
      this.isLogged = false;
      this.nombreUsuario = '';
    }

    // En una aplicación real, aquí podrías cargar las estadísticas desde un servicio
    // this.loadSystemStats();
  }

  // Método para cargar estadísticas del sistema (implementación futura)
  // private loadSystemStats() {
  //   this.someService.getSystemStats().subscribe(
  //     stats => this.systemStats = stats,
  //     error => console.error('Error loading system stats:', error)
  //   );
  // }
}

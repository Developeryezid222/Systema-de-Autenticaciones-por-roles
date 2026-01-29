import { Component, HostListener, OnInit } from '@angular/core';
import { Opcion } from '../security/opcion';
import { TokenService } from '../security/token.service';
import { Router, RouterLink } from '@angular/router';
import { AppMaterialModule } from '../app.material.module';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { animate, state, style, transition, trigger } from '@angular/animations';

interface OpcionGrupo {
  titulo: string;
  tipo: number;
  opciones: Opcion[];
  expandido: boolean;
  icono?: string; // Añadida la propiedad icono como opcional
}

@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [AppMaterialModule, FormsModule, CommonModule, RouterLink],
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css'],
  animations: [
    trigger('expandCollapse', [
      state('collapsed', style({
        height: '0',
        overflow: 'hidden'
      })),
      state('expanded', style({
        height: '*'
      })),
      transition('collapsed <=> expanded', animate('300ms ease-in-out'))
    ])
  ]
})
export class MenuComponent implements OnInit {
  isMenuOpen = false;
  isMobile = false;
  opcionesAgrupadas: OpcionGrupo[] = [];
  nombreUsuario = "";

  // Mapeo de tipos a títulos e iconos
  tipoTitulos: {[key: number]: {titulo: string, icono: string}} = {
    1: {titulo: "Configuración", icono: "fa-cog"},
    2: {titulo: "Reportes", icono: "fa-chart-bar"},
    3: {titulo: "Administración", icono: "fa-users-cog"}
    // Puedes agregar más tipos aquí
  };

  constructor(private tokenService: TokenService, private router: Router) {
    console.log("MenuComponent >>> constructor >>> " + this.tokenService.getToken());
  }

  ngOnInit() {
    this.checkScreenSize();
    console.log("MenuComponent >>> ngOnInit >>> ");

    // Obtener todas las opciones
    const todasOpciones = this.tokenService.getOpciones();
    
    // Agrupar por tipo
    this.organizarOpcionesPorTipo(todasOpciones);
    
    this.nombreUsuario = this.tokenService.getUserNameComplete() || '';
  }

  organizarOpcionesPorTipo(opciones: Opcion[]): void {
    // Obtener tipos únicos
    const tipos = [...new Set(opciones.map(opcion => opcion.tipo))];
    
    // Crear grupos de opciones
    this.opcionesAgrupadas = tipos.map(tipo => {
      const opcionesFiltradas = opciones.filter(opcion => opcion.tipo === tipo);
      
      // Solo crear el grupo si hay opciones disponibles
      if (opcionesFiltradas.length > 0) {
        const tipoInfo = this.tipoTitulos[tipo as keyof typeof this.tipoTitulos];
        const titulo = tipoInfo ? tipoInfo.titulo : `Grupo ${tipo}`;
        const icono = tipoInfo ? tipoInfo.icono : 'fa-layer-group'; // Icono predeterminado
        
        return {
          titulo: titulo,
          tipo: tipo,
          opciones: opcionesFiltradas,
          expandido: false,
          icono: icono
        };
      }
      return null;
    }).filter(grupo => grupo !== null) as OpcionGrupo[];
  }

  toggleGrupo(grupo: OpcionGrupo): void {
    grupo.expandido = !grupo.expandido;
  }

  @HostListener('window:resize', [])
  checkScreenSize() {
    this.isMobile = window.innerWidth < 768;
    this.isMenuOpen = !this.isMobile;
  }

  onLogOut(): void {
    this.tokenService.logOut();
    // Redirigir a login después del logout
    this.router.navigate(['/login']);
  }

  toggleMenu() {
    this.isMenuOpen = !this.isMenuOpen;
  }

  closeMenu(event: MouseEvent) {
    if (this.isMobile) {
      this.isMenuOpen = false;
    }
  }
}
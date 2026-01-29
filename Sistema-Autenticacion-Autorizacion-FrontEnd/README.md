# SISTEMA DE AUTENTICACIÓN Y AUTORIZACIÓN FRONTEND

## Descripción

Este proyecto implementa el frontend para un sistema completo de autenticación y autorización de usuarios desarrollado con Angular. Proporciona una interfaz intuitiva para el registro, inicio de sesión, gestión de perfiles y acceso a recursos protegidos basados en roles y permisos.

## Características principales

- **Autenticación de usuarios**: Registro e inicio de sesión seguros
- **Recuperación de contraseña**: Sistema de restablecimiento vía correo electrónico
- **Panel de perfil**: Gestión de información personal del usuario
- **Control de acceso basado en roles**: Visualización dinámica de interfaces según los permisos
- **Gestión de sesiones**: Control de tiempos de sesión y cierre de sesión automático
- **Diseño responsive**: Adaptable a diferentes dispositivos

## Tecnologías utilizadas

- Angular (framework principal)
- TypeScript
- Angular Material (componentes UI)
- RxJS (programación reactiva)
- JWT (manejo de tokens)
- CSS/SCSS (estilos)
- Angular Router (navegación)
- Interceptores HTTP para manejo de autenticación

## Requisitos previos

- Node.js (v14.0.0 o superior)
- NPM (v6.0.0 o superior)
- Angular CLI (`npm install -g @angular/cli`)
- API Backend para sistema de autenticación y autorización

## Instalación

1. Clonar el repositorio:
   ```bash
   git clone git@github.com:Developeryezid222/Systema-Autenticacion.git
   cd Sistema-Autenticacion-FrontEnd
   ```

2. Instalar dependencias:
   ```bash
   npm install
   ```

3. Configurar variables de entorno:
   - Revisar y ajustar configuraciones en `app.config.ts` y otros archivos de configuración

4. Iniciar el servidor de desarrollo:
   ```bash
   ng serve -o
   ```

## Estructura del proyecto

```
/
├── .angular                # Configuración interna de Angular
├── .vscode                 # Configuración de Visual Studio Code
├── node_modules           # Dependencias instaladas
├── public                 # Archivos públicos
├── src                    # Código fuente de la aplicación
│   ├── app                # Componentes y lógica principal
│   │   ├── auth           # Componentes de autenticación
│   │   ├── features       # Características principales
│   │   ├── guards         # Guards de rutas
│   │   ├── index          # Índices para exportación
│   │   ├── interceptors   # Interceptores HTTP
│   │   ├── menu           # Componentes de menú
│   │   ├── modules        # Módulos de la aplicación
│   │   ├── security       # Configuración de seguridad
│   │   ├── service        # Servicios de la aplicación
│   │   ├── app.component.css
│   │   ├── app.component.html
│   │   ├── app.component.ts
│   │   ├── app.config.ts
│   │   ├── app.material.module.ts
│   │   ├── app.routes.ts
│   │   └── app.settings.ts
│   ├── assets             # Recursos estáticos
│   ├── favicon.ico
│   ├── index.html         # Archivo HTML principal
│   ├── main.ts            # Punto de entrada de la aplicación
│   ├── styles.css         # Estilos globales
│   └── styles.scss        # Estilos SCSS
├── .editorconfig
├── .gitignore
├── angular.json           # Configuración de Angular
├── package-lock.json
├── package.json           # Dependencias y scripts
├── README.md
├── tsconfig.app.json      # Configuración de TypeScript para la app
├── tsconfig.json          # Configuración general de TypeScript
└── tsconfig.spec.json     # Configuración de TypeScript para pruebas
```

## Flujo de autenticación

1. El usuario inicia sesión con credenciales
2. El backend valida y devuelve tokens JWT
3. Los tokens se almacenan en localStorage/sessionStorage
4. Los interceptores incluyen tokens en cabeceras HTTP para acceder a recursos protegidos
5. Se implementa renovación automática de tokens expirados

## Manejo de roles y permisos

El sistema implementa control de acceso basado en roles (RBAC):

- Los permisos se obtienen del backend tras autenticación exitosa
- Guardas de ruta verifican permisos antes de permitir acceso a componentes
- Componentes específicos para cada rol (Admin, Usuario, etc.)
- Menús dinámicos que muestran opciones según permisos del usuario

## Scripts disponibles

- `ng serve`: Inicia el servidor de desarrollo
- `ng build`: Compila el proyecto para producción
- `ng test`: Ejecuta pruebas unitarias con Karma
- `ng lint`: Verifica estilo de código
- `ng e2e`: Ejecuta pruebas end-to-end

## Dependencias principales

- @angular/core, @angular/common, @angular/router: Framework Angular
- @angular/material, @angular/cdk: Componentes UI de Material Design
- rxjs: Biblioteca para programación reactiva
- jwt-decode: Decodificación de tokens JWT
- otros paquetes específicos definidos en package.json



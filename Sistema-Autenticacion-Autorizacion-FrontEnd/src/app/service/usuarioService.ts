import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AppSettings } from '../app.settings';
import { UsuarioDTO } from '../modules/DTO/UsuarioDTO';
import { Rol } from '../modules/rol.model';

const baseUrlUtil = AppSettings.API_ENDPOINT + '/usuarios';


@Injectable({
    providedIn: 'root'
})
export class UsuarioService {
    constructor(private http: HttpClient) { }
    listarUsuariosConFiltros(idRol: number, estado: number, filtro: string): Observable<UsuarioDTO[]> {
        const url = `${baseUrlUtil}/listar?idRol=${idRol}&estado=${estado}&filtro=${filtro}`;
        return this.http.get<UsuarioDTO[]>(url);
    }
    obtenerUsuarioPorId(idUsuario: number): Observable<UsuarioDTO> {
        const url = `${baseUrlUtil}/obtenerPorId/${idUsuario}`;
        return this.http.get<UsuarioDTO>(url);
    }
    guardarUsuario(usuario: any): Observable<any> {
        return this.http.post(`${baseUrlUtil}/registrar`, usuario);
    }
    actualizarUsuario(usuario: any): Observable<any> {
        return this.http.put(`${baseUrlUtil}/actualizar`, usuario);
    }
    obtenerRolesUsuario(idUsuario: number): Observable<Rol[]> {
        return this.http.get<Rol[]>(`${baseUrlUtil}/roles/${idUsuario}`);
    }

    obtenerRolesUsuarioIds(idUsuario: number): Observable<number[]> {
        return this.http.get<number[]>(`${baseUrlUtil}/roles/ids/${idUsuario}`);
    }

    actualizarRolesUsuario(idUsuario: number, roles: number[]): Observable<any> {
        return this.http.put(`${baseUrlUtil}/actualizarRolUsuario/${idUsuario}`, roles,
            { responseType: 'text' });
    }

    eliminarUsuario(idUsuario: number): Observable<string> {
        return this.http.delete(`${baseUrlUtil}/eliminar/${idUsuario}`, { responseType: 'text' });
    }
}


import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { Rol } from "../modules/rol.model";
import { AppSettings } from "../app.settings";
import { TipoDocumento } from "../modules/tipoDocumento.model";

const baseUrlUtil = AppSettings.API_ENDPOINT+ '/util';

@Injectable({
    providedIn: 'root'
})
export class UtilService {
    constructor(private http: HttpClient) { }

    listarRoles():Observable<Rol[]>{
        return this.http.get<Rol[]>(`${baseUrlUtil}/roles`);
    }
    listarTiposDocumento():Observable<TipoDocumento[]>{
        return this.http.get<TipoDocumento[]>(`${baseUrlUtil}/tiposDocumentos`);
    }
}
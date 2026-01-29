import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Rol } from "../modules/rol.model";
import { RolConOpcionDTO } from "../modules/DTO/RolConOpcionDTO";
import { AppSettings } from "../app.settings";
import { HttpClient } from "@angular/common/http";

const baseUrlUtil = AppSettings.API_ENDPOINT+ '/roles';

@Injectable({
    providedIn: 'root'
})

export class RolService {
    constructor(private http: HttpClient) { }

    listarRolesConOpciones():Observable<RolConOpcionDTO[]>{
        return this.http.get<RolConOpcionDTO[]>(`${baseUrlUtil}/listaRolOpciones`);
    }
}
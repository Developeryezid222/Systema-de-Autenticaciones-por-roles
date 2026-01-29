import { Rol } from "./rol.model";

export interface RolConAsignacion extends Rol {
    asignado: boolean;
}
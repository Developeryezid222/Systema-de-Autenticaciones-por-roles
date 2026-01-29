export interface UsuarioDTO {
    idUsuario: number;
    nombres: string;
    apellidos: string;
    celular: string;
    correo: string;
    estado: number;
    foto?: string;
    login: string;
    numDoc: string;
    tipoDocumento: string;
    rolesString: string;
}
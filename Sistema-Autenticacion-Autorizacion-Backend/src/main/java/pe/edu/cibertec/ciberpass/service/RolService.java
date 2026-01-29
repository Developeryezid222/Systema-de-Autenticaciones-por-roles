package pe.edu.cibertec.ciberpass.service;

import java.util.List;

import pe.edu.cibertec.ciberpass.entity.Rol;
import pe.edu.cibertec.ciberpass.entity.dto.RolOpcionesDTO;

public interface RolService {
	public abstract List<Rol> listarRoles();
	public List<RolOpcionesDTO> listarRolesOpciones();
}

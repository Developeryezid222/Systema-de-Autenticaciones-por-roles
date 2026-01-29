package pe.edu.cibertec.ciberpass.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import pe.edu.cibertec.ciberpass.entity.Rol;
import pe.edu.cibertec.ciberpass.entity.dto.RolOpcionesDTO;
import pe.edu.cibertec.ciberpass.repository.RolRepository;
import pe.edu.cibertec.ciberpass.service.RolService;



@Service
public class RolServiceImpl implements RolService {


	private final RolRepository rolRepository;

    public RolServiceImpl(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Override
	public List<Rol> listarRoles() {
		return rolRepository.findAll();
	}

	@Override
	public List<RolOpcionesDTO> listarRolesOpciones() {
		return rolRepository.listarRolesUsuarios().stream()
				.map(obj -> new RolOpcionesDTO(
						((Number) obj[0]).intValue(),  // idRol
						(String) obj[1],               // nombre
						((Number) obj[2]).intValue(),  // estado
						(String) obj[3]                // opciones concatenadas
				))
				.toList();
	}
}

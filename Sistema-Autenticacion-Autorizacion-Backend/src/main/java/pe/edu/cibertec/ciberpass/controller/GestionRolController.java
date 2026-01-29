package pe.edu.cibertec.ciberpass.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.cibertec.ciberpass.entity.dto.RolOpcionesDTO;
import pe.edu.cibertec.ciberpass.service.RolService;
import pe.edu.cibertec.ciberpass.util.AppSettings;

@RestController
@RequestMapping("url/roles")
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class GestionRolController {
	
	@Autowired
	private RolService rolService;
	
	
	@GetMapping("/listaRolOpciones")
	public List<RolOpcionesDTO> listaRolConOpciones(){
		return rolService.listarRolesOpciones();
	}
	

}

package pe.edu.cibertec.ciberpass.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.cibertec.ciberpass.entity.Rol;
import pe.edu.cibertec.ciberpass.entity.TipoDocumento;
import pe.edu.cibertec.ciberpass.service.RolService;
import pe.edu.cibertec.ciberpass.service.TipoDocService;
import pe.edu.cibertec.ciberpass.util.AppSettings;

@RestController
@RequestMapping("url/util")
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class UtilController {
	
	@Autowired
	private RolService rolService;
	
	@Autowired
	private TipoDocService tipoDocService;
	
	@GetMapping("/roles")
	@ResponseBody
	public List<Rol> listadoRoles(){
		return rolService.listarRoles();
	}
	
	@GetMapping("/tiposDocumentos")
	@ResponseBody
	public List<TipoDocumento> listaTipoDoc() {
		return tipoDocService.listaTodos();
	}
}

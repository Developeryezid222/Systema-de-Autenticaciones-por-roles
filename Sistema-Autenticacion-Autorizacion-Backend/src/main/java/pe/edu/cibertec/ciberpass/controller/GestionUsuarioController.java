package pe.edu.cibertec.ciberpass.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.cibertec.ciberpass.entity.Rol;
import pe.edu.cibertec.ciberpass.entity.dto.UsuarioDTO;
import pe.edu.cibertec.ciberpass.entity.dto.UsuarioRegistroDTO;
import pe.edu.cibertec.ciberpass.entity.dto.UsuarioRespuestaDTO;
import pe.edu.cibertec.ciberpass.entity.dto.UsuarioUpdateDTO;
import pe.edu.cibertec.ciberpass.security.UsuarioPrincipal;
import pe.edu.cibertec.ciberpass.service.UsuarioService;
import pe.edu.cibertec.ciberpass.util.AppSettings;

@RestController
@RequestMapping("url/usuarios")
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)

public class GestionUsuarioController {
	
	@Autowired
    private UsuarioService usuarioService;
	
	
	
	@GetMapping("/listar")
	@ResponseBody
	public List<UsuarioDTO> listadoUsuarios(
	        @RequestParam(required = false, defaultValue = "-1") Integer idRol,
	        @RequestParam(required = false, defaultValue = "-1") Integer estado,
	        @RequestParam(required = false, defaultValue = "") String filtro) {
	    return usuarioService.listarUsuariosConFiltros(idRol, estado, filtro);
	}
	
	@GetMapping("/obtenerPorId/{idUsuario}")
	@ResponseBody
	public UsuarioDTO obtenerUsuarioPorId(@PathVariable Integer idUsuario) {
	    return usuarioService.buscarUsuarioPorId(idUsuario);
	}
	
	 
	@PostMapping("/registrar")
	@ResponseBody
	public UsuarioRespuestaDTO registrarUsuario(@RequestBody UsuarioRegistroDTO usuarioDTO) {
	    return usuarioService.registrarUsuario(usuarioDTO);
	}
	
	// Actualizar usuario
    @PutMapping("/actualizar")
    public ResponseEntity<UsuarioRespuestaDTO> actualizarUsuario(@RequestBody UsuarioUpdateDTO dto) {
        UsuarioRespuestaDTO respuesta = usuarioService.actualizarUsuario(dto);
        return ResponseEntity.ok(respuesta);
    } 
    
    @GetMapping("/roles/{idUsuario}")
    public ResponseEntity<List<Rol>> listarRolesDeUsuario(@PathVariable Integer idUsuario) {
        List<Rol> roles = usuarioService.traerRolesDeUsuario(idUsuario);
        return ResponseEntity.ok(roles);
    }
    
    
    @GetMapping("/roles/ids/{idUsuario}")
    public ResponseEntity<List<Integer>> listarIdsRolesDeUsuario(@PathVariable Integer idUsuario) {
        List<Integer> ids = usuarioService.traerIdsRolesDeUsuario(idUsuario);
        return ResponseEntity.ok(ids);
    }
    
    @PutMapping("/actualizarRolUsuario/{idUsuario}")
    public ResponseEntity<String> actualizarRolesUsuario(
            @PathVariable int idUsuario,
            @RequestBody List<Integer> nuevosRolesIds) {

        usuarioService.actualizarRolesUsuario(idUsuario, nuevosRolesIds);
        return ResponseEntity.ok("Roles actualizados correctamente");
    }
    
    
    @DeleteMapping("/eliminar/{idUsuario}")
    public ResponseEntity<String> eliminarUsuario(
            @PathVariable int idUsuario,
            @AuthenticationPrincipal UsuarioPrincipal usuarioLogeado) {

        int idUsuarioLogeado = usuarioLogeado.getIdUsuario();
        usuarioService.eliminarUsuario(idUsuario, idUsuarioLogeado);

        return ResponseEntity.ok("Usuario eliminado correctamente");
    }
    

	
}

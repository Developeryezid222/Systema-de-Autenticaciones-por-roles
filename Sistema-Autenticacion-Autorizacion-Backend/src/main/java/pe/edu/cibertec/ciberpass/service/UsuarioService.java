package pe.edu.cibertec.ciberpass.service;

import java.util.List;

import pe.edu.cibertec.ciberpass.entity.Opcion;
import pe.edu.cibertec.ciberpass.entity.Rol;
import pe.edu.cibertec.ciberpass.entity.Usuario;
import pe.edu.cibertec.ciberpass.entity.dto.UsuarioDTO;
import pe.edu.cibertec.ciberpass.entity.dto.UsuarioRegistroDTO;
import pe.edu.cibertec.ciberpass.entity.dto.UsuarioRespuestaDTO;
import pe.edu.cibertec.ciberpass.entity.dto.UsuarioUpdateDTO;

public interface UsuarioService {
	public abstract List<Opcion> traerEnlacesDeUsuario(int idUsuario);

	public List<Rol> traerRolesDeUsuario(int idUsuario);
	
	public List<Integer> traerIdsRolesDeUsuario(int idUsuario);
	
	public void actualizarRolesUsuario(int idUsuario, List<Integer> nuevosRolesIds);
	
	public void eliminarUsuario(int idUsuario, int idUsuarioLogeado);

	public abstract Usuario buscaPorLogin(String login);

	public abstract Usuario buscaPorId(int idUsuario);
	
	public abstract Usuario authenticate(String login, String password);
	
	public Usuario buscarPorNumeroDocumento(String numDoc);
	
	public List<UsuarioDTO> listarUsuariosConFiltros(Integer idRol, Integer estado, String filtro);
	
	UsuarioDTO buscarUsuarioPorId(Integer idUsuario);
	
	public UsuarioRespuestaDTO registrarUsuario(UsuarioRegistroDTO dto);
	
	public UsuarioRespuestaDTO actualizarUsuario(UsuarioUpdateDTO dto);
	
	
	
}

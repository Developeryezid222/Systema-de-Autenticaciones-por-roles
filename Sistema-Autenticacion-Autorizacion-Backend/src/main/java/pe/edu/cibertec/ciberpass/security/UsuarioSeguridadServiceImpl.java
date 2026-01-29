package pe.edu.cibertec.ciberpass.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.extern.apachecommons.CommonsLog;
import pe.edu.cibertec.ciberpass.entity.Opcion;
import pe.edu.cibertec.ciberpass.entity.Rol;
import pe.edu.cibertec.ciberpass.entity.Usuario;
import pe.edu.cibertec.ciberpass.repository.UsuarioRepository;

@Service
@CommonsLog
public class UsuarioSeguridadServiceImpl implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Transactional
	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		log.info(">>>loadUserByUsername >>> " + login);
		UserDetails userDetails = null;
		try {
			Usuario objUsuario = usuarioRepository.findByLogin(login);
			if (objUsuario != null) {
				
				if(objUsuario.getEstado() != 1) {
					throw new UsernameNotFoundException("Usuario inactivo, no puede iniciar sesión");
				}
					
				
				log.info("==> Login =========== " + objUsuario);

				List<Rol> lstRol = usuarioRepository.traerRolesDeUsuario(objUsuario.getIdUsuario());
				log.info("==> Roles =========== " + lstRol);
				
				List<Opcion> lstOpciones = usuarioRepository.traerEnlacesDeUsuario(objUsuario.getIdUsuario());
				log.info("==> Opciones =========== " + lstOpciones);

				userDetails = UsuarioPrincipal.build(objUsuario, lstRol, lstOpciones);
			}
		} catch (IndexOutOfBoundsException e) {
			throw new UsernameNotFoundException("Wrong username");
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new UsernameNotFoundException("Database Error");
		} catch (Exception e) {
			e.printStackTrace();
			throw new UsernameNotFoundException("Unknown Error");
		}
		return userDetails;
	}

}

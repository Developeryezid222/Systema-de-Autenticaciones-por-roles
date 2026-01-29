package pe.edu.cibertec.ciberpass.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.cibertec.ciberpass.entity.Opcion;
import pe.edu.cibertec.ciberpass.entity.Rol;
import pe.edu.cibertec.ciberpass.entity.TipoDocumento;
import pe.edu.cibertec.ciberpass.entity.Usuario;
import pe.edu.cibertec.ciberpass.entity.dto.UsuarioDTO;
import pe.edu.cibertec.ciberpass.entity.dto.UsuarioRegistroDTO;
import pe.edu.cibertec.ciberpass.entity.dto.UsuarioRespuestaDTO;
import pe.edu.cibertec.ciberpass.entity.dto.UsuarioUpdateDTO;
import pe.edu.cibertec.ciberpass.repository.TipoDocRepository;
import pe.edu.cibertec.ciberpass.repository.UsuarioRepository;
import pe.edu.cibertec.ciberpass.service.UsuarioService;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired
	private UsuarioRepository repository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private TipoDocRepository tipoDocumentoRepository;

	@Override
	public List<Opcion> traerEnlacesDeUsuario(int idUsuario) {
		return repository.traerEnlacesDeUsuario(idUsuario);
	}

	@Override
	public List<Rol> traerRolesDeUsuario(int idUsuario) {
		return repository.traerRolesDeUsuario(idUsuario);
	}

	@Override
	public Usuario buscaPorLogin(String login) {
		return repository.findByLogin(login);
	}

	@Override
	public Usuario buscaPorId(int idUsuario) {
		return repository.findById(idUsuario).orElse(null);
	}

	@Override
	public Usuario authenticate(String login, String password) {
		return repository.findByLoginAndPassword(login, password);
	}

	@Override
	public Usuario buscarPorNumeroDocumento(String numDoc) {
		return repository.findByNumDoc(numDoc).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
	}

	@Override
	@Transactional(readOnly = true)
	public List<UsuarioDTO> listarUsuariosConFiltros(Integer idRol, Integer estado, String filtro) {
		if (idRol == null)
			idRol = -1;
		if (estado == null)
			estado = -1;
		if (filtro == null)
			filtro = "";

		List<Object[]> resultados = repository.listarUsuariosConFiltros(idRol, estado, filtro);
		return resultados.stream().map(this::mapearObjetoAUsuarioDTOConRolesConcatenados).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public UsuarioDTO buscarUsuarioPorId(Integer idUsuario) {
		Object resultado = repository.buscarUsuarioPorId(idUsuario);
		if (resultado == null) {
			return null; // o lanzar excepción si prefieres
		}
		return mapearObjetoAUsuarioDTOConRolesConcatenados((Object[]) resultado);
	}

	private UsuarioDTO mapearObjetoAUsuarioDTOConRolesConcatenados(Object[] obj) {
		UsuarioDTO dto = new UsuarioDTO((Integer) obj[0], // id_usuario
				(String) obj[1], // nombres
				(String) obj[2], // apellidos
				(String) obj[3], // celular
				(String) obj[4], // correo
				(Integer) obj[5], // estado
				(String) obj[6], // foto
				(String) obj[7], // login
				(String) obj[8], // num_doc
				(Integer) obj[9], // id_tipo_doc
				(String) obj[10] // tipo_documento
		);

		// Agregar roles concatenados
		dto.setRolesString((String) obj[11]);
		return dto;
	}

	@Override
	public UsuarioRespuestaDTO registrarUsuario(UsuarioRegistroDTO dto) {
	    // Validar existencia del tipo de documento
	    if (!tipoDocumentoRepository.existsById(dto.getIdTipoDoc())) {
	        throw new RuntimeException("Tipo de documento no encontrado");
	    }

	    // Crear usuario
	    Usuario usuario = new Usuario();
	    usuario.setNombres(dto.getNombres());
	    usuario.setApellidos(dto.getApellidos());
	    usuario.setLogin(dto.getLogin());
	    usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
	    usuario.setCorreo(dto.getCorreo());
	    usuario.setCelular(dto.getCelular());
	    usuario.setNumDoc(dto.getNumDoc());
	    usuario.setFoto(dto.getNombres() + ".jpeg");
	    usuario.setEstado(dto.getEstado());
	    usuario.setFechaRegistro(LocalDateTime.now());
	    usuario.setFechaActualizacion(LocalDateTime.now());

	    // Solo referenciar tipoDocumento por ID sin cargar toda la entidad
	    TipoDocumento tipoDoc = new TipoDocumento();
	    tipoDoc.setIdTipoDoc(dto.getIdTipoDoc());
	    usuario.setTipodocumento(tipoDoc);

	    Usuario guardado = repository.save(usuario);

	    return new UsuarioRespuestaDTO(guardado.getIdUsuario(), guardado.getNombreCompleto());
	}

	
	@Override
	public UsuarioRespuestaDTO actualizarUsuario(UsuarioUpdateDTO dto) {
	    // Validar existencia de usuario
	    if (!repository.existsById(dto.getIdUsuario())) {
	        throw new RuntimeException("Usuario no encontrado");
	    }

	    // Validar existencia de tipo de documento
	    if (!tipoDocumentoRepository.existsById(dto.getIdTipoDoc())) {
	        throw new RuntimeException("Tipo de documento no encontrado");
	    }

	    // Encriptar contraseña solo si viene
	    String encodedPassword = null;
	    if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
	        encodedPassword = passwordEncoder.encode(dto.getPassword());
	    }

	    // Ejecutar update directo
	    int filasActualizadas = repository.actualizarUsuarioParcial(
	        dto.getIdUsuario(),
	        dto.getNombres(),
	        dto.getApellidos(),
	        dto.getLogin(),
	        dto.getCorreo(),
	        dto.getCelular(),
	        dto.getNumDoc(),
	        dto.getNombres() + ".jpeg",
	        dto.getEstado(),
	        LocalDateTime.now(),
	        encodedPassword,
	        dto.getIdTipoDoc()
	    );

	    if (filasActualizadas == 0) {
	        throw new RuntimeException("Error al actualizar usuario");
	    }

	    // Retornar solo ID y nombre completo
	    return new UsuarioRespuestaDTO(dto.getIdUsuario(), dto.getNombres() + " " + dto.getApellidos());
	}


	@Override
	public List<Integer> traerIdsRolesDeUsuario(int idUsuario) {
		return repository.traerIdsRolesDeUsuario(idUsuario);
	}
	
	@Override
	public void actualizarRolesUsuario(int idUsuario, List<Integer> nuevosRolesIds) {
		repository.eliminarRolesPorUsuario(idUsuario);
		
		// Insertar nuevos roles solo si la lista no está vacía
	    if (nuevosRolesIds != null && !nuevosRolesIds.isEmpty()) {
	        for (Integer idRol : nuevosRolesIds) {
	            repository.insertarUsuarioRol(idUsuario, idRol);
	        }
	    }
	}
	
	
	@Override
	public void eliminarUsuario(int idUsuario, int idUsuarioLogeado) {
	    // Validar que no sea el mismo que está logeado
	    if (idUsuario == idUsuarioLogeado) {
	        throw new RuntimeException("No puedes eliminar tu propio usuario mientras estás logeado");
	    }

	    // Buscar usuario
	    Usuario usuario = repository.findById(idUsuario)
	            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

	    // Opcional: eliminar roles primero (si aplica tu lógica)
	    repository.eliminarRolesPorUsuario(idUsuario);

	    // Eliminar usuario
	    repository.delete(usuario);
	}
 
}

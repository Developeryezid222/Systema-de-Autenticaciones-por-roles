package pe.edu.cibertec.ciberpass.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pe.edu.cibertec.ciberpass.entity.Opcion;
import pe.edu.cibertec.ciberpass.entity.Rol;
import pe.edu.cibertec.ciberpass.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
	Optional<Usuario> findByCorreo(String correo);

	@Query("Select p from Opcion p, RolHasOpcion pr, Rol r, UsuarioHasRol u where  p.idOpcion = pr.opcion.idOpcion and pr.rol.idRol = r.idRol and r.idRol = u.rol.idRol and u.usuario.idUsuario = :var_idUsuario")
	public abstract List<Opcion> traerEnlacesDeUsuario(@Param("var_idUsuario") int idUsuario);

	@Query("Select r from Rol r, UsuarioHasRol u where r.idRol = u.rol.idRol and u.usuario.idUsuario = :var_idUsuario")
	List<Rol> traerRolesDeUsuario(@Param("var_idUsuario") int idUsuario);

	@Query("SELECT r.idRol from Rol r, UsuarioHasRol u WHERE r.idRol = u.rol.idRol AND u.usuario.idUsuario = :var_idUsuario")
	public List<Integer> traerIdsRolesDeUsuario(@Param("var_idUsuario") int idUsuario);

	@Modifying(clearAutomatically = true)
	@Query("DELETE FROM UsuarioHasRol uhr WHERE uhr.usuarioHasRolPk.idUsuario = :idUsuario")
	void eliminarRolesPorUsuario(@Param("idUsuario") int idUsuario);

	@Modifying(clearAutomatically = true)
	@Query(value = "INSERT INTO usuario_tiene_rol (idUsuario, idRol) VALUES (:idUsuario, :idRol)", nativeQuery = true)
	void insertarUsuarioRol(@Param("idUsuario") int idUsuario, @Param("idRol") int idRol);

	// =========================================================================
	// BÚSQUEDA DE USUARIOS
	// =========================================================================

	Usuario findByLogin(String login);

	Usuario findByLoginAndPassword(String login, String password);

	Optional<Usuario> findByNumDoc(String numDoc);

	// Query para obtener usuarios con roles concatenados (para mostrar en tabla)
	@Query(value = """
			SELECT
			    u.id_usuario,
			    u.nombres,
			    u.apellidos,
			    u.celular,
			    u.correo,
			    u.estado,
			    u.foto,
			    u.login,
			    u.num_doc,
			    u.id_tipo_doc,
			    td.descripcion as tipo_documento,
			    COALESCE(GROUP_CONCAT(r.nombre SEPARATOR ', '), 'Sin Rol') as roles_concatenados
			FROM usuario u
			LEFT JOIN tipo_documento td ON u.id_tipo_doc = td.id_tipo_doc
			LEFT JOIN usuario_tiene_rol utr ON u.id_usuario = utr.idUsuario
			LEFT JOIN rol r ON utr.idRol = r.idRol AND r.estado = 1
			WHERE (:idRol = -1 OR EXISTS (
			    SELECT 1 FROM usuario_tiene_rol utr2
			    INNER JOIN rol r2 ON utr2.idRol = r2.idRol
			    WHERE utr2.idUsuario = u.id_usuario AND r2.idRol = :idRol AND r2.estado = 1
			))
			AND (:estado = -1 OR u.estado = :estado)
			AND (:filtro = '' OR LOWER(CONCAT(u.nombres, ' ', u.apellidos)) LIKE LOWER(CONCAT('%', :filtro, '%'))
			     OR LOWER(u.correo) LIKE LOWER(CONCAT('%', :filtro, '%'))
			     OR LOWER(u.login) LIKE LOWER(CONCAT('%', :filtro, '%')))
			GROUP BY u.id_usuario, u.nombres, u.apellidos, u.celular, u.correo,
			         u.estado, u.foto, u.login, u.num_doc, u.id_tipo_doc, td.descripcion
			ORDER BY u.nombres, u.apellidos
			""", nativeQuery = true)
	List<Object[]> listarUsuariosConFiltros(@Param("idRol") Integer idRol, @Param("estado") Integer estado,
			@Param("filtro") String filtro);

	@Query(value = """
			SELECT
			    u.id_usuario,
			    u.nombres,
			    u.apellidos,
			    u.celular,
			    u.correo,
			    u.estado,
			    u.foto,
			    u.login,
			    u.num_doc,
			    u.id_tipo_doc,
			    td.descripcion AS tipo_documento,
			    COALESCE(GROUP_CONCAT(r.nombre SEPARATOR ', '), 'Sin Rol') AS roles_concatenados
			FROM usuario u
			LEFT JOIN usuario_tiene_rol ur ON ur.idUsuario = u.id_usuario
			LEFT JOIN rol r ON r.idRol = ur.idRol AND r.estado = 1
			LEFT JOIN tipo_documento td ON td.id_tipo_doc = u.id_tipo_doc
			WHERE u.id_usuario = :idUsuario
			GROUP BY u.id_usuario, u.nombres, u.apellidos, u.celular, u.correo,
			         u.estado, u.foto, u.login, u.num_doc, u.id_tipo_doc, td.descripcion
			""", nativeQuery = true)
	Object buscarUsuarioPorId(@Param("idUsuario") Integer idUsuario);

	@Modifying
	@Query("""
			    UPDATE Usuario u
			    SET u.nombres = :nombres,
			        u.apellidos = :apellidos,
			        u.login = :login,
			        u.correo = :correo,
			        u.celular = :celular,
			        u.numDoc = :numDoc,
			        u.foto = :foto,
			        u.estado = :estado,
			        u.fechaActualizacion = :fechaActualizacion,
			        u.password = COALESCE(:password, u.password),
			        u.tipodocumento.idTipoDoc = :idTipoDoc
			    WHERE u.idUsuario = :idUsuario
			""")
	int actualizarUsuarioParcial(@Param("idUsuario") int idUsuario, @Param("nombres") String nombres,
			@Param("apellidos") String apellidos, @Param("login") String login, @Param("correo") String correo,
			@Param("celular") String celular, @Param("numDoc") String numDoc, @Param("foto") String foto,
			@Param("estado") Integer estado, @Param("fechaActualizacion") LocalDateTime fechaActualizacion,
			@Param("password") String password, @Param("idTipoDoc") Integer idTipoDoc);

}

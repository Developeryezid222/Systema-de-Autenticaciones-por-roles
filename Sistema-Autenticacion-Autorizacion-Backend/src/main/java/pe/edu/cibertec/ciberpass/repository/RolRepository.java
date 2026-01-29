package pe.edu.cibertec.ciberpass.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pe.edu.cibertec.ciberpass.entity.Rol;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {
	
	@Query(value = """
			SELECT 
			r.idRol,
			r.nombre,
			r.estado,
			COALESCE(GROUP_CONCAT(o.nombre SEPARATOR ', '), 'Sin opción') AS opciones_concatenados
			FROM
			rol r
			LEFT JOIN rol_has_opcion rho ON r.idRol = rho.idRol
			LEFT JOIN opcion o ON o.id_opcion = rho.idOpcion
			GROUP BY r.idRol, r.nombre, r.estado
			""",nativeQuery = true)
	List<Object[]> listarRolesUsuarios();
}

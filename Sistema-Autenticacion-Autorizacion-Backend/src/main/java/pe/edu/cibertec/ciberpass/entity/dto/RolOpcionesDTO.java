package pe.edu.cibertec.ciberpass.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RolOpcionesDTO {
	private Integer idRol;
	private String nombre;
	private Integer estado;
	private String opcionesString;
}

package pe.edu.cibertec.ciberpass.entity.dto;

import lombok.Data;

@Data
public class UsuarioRespuestaDTO {
	private Integer idUsuario;
	private String nombreCompleto;
	
	
	 public UsuarioRespuestaDTO(Integer idUsuario, String nombreCompleto) {
	        this.idUsuario = idUsuario;
	        this.nombreCompleto = nombreCompleto;
	    }
}

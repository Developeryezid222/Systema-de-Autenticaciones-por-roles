package pe.edu.cibertec.ciberpass.entity.dto;

import lombok.Data;

@Data
public class UsuarioRegistroDTO {
	 private String nombres;
	    private String apellidos;
	    private String login;
	    private String password; // en texto plano, se encripta en backend
	    private String correo;
	    private String celular;
	    private Integer idTipoDoc;
	    private String numDoc;
	    private Integer estado;
}

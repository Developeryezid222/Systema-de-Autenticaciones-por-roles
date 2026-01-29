package pe.edu.cibertec.ciberpass.entity.dto;

import lombok.Data;

@Data
public class UsuarioUpdateDTO {
	private Integer idUsuario;
	private String nombres;
	private String apellidos;
	private String correo;
	private String login;
	private String celular;
	private String numDoc;
	private Integer idTipoDoc;
	private Integer estado;
	private String password;
}

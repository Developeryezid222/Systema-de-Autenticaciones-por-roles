package pe.edu.cibertec.ciberpass.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
	 private Integer idUsuario;
	    private String nombres;
	    private String apellidos;
	    private String celular;
	    private String correo;
	    private Integer estado;
	    private String foto;
	    private String login;
	    private String numDoc;
	    private Integer idTipoDoc;
	    private String tipoDocumento;
	    private String rolesString; // String concatenado de roles para mostrar en tabla
	    
	    // Constructor para query básico (sin roles)
	    public UsuarioDTO(Integer idUsuario, String nombres, String apellidos, String celular, 
	                     String correo, Integer estado, String foto, String login, String numDoc, 
	                     Integer idTipoDoc, String tipoDocumento) {
	        this.idUsuario = idUsuario;
	        this.nombres = nombres;
	        this.apellidos = apellidos;
	        this.celular = celular;
	        this.correo = correo;
	        this.estado = estado;
	        this.foto = foto;
	        this.login = login;
	        this.numDoc = numDoc;
	        this.idTipoDoc = idTipoDoc;
	        this.tipoDocumento = tipoDocumento;
	    }
}

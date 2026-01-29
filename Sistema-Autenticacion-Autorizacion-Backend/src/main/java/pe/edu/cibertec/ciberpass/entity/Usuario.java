package pe.edu.cibertec.ciberpass.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuario")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario")
	private int idUsuario;

	@Column(name = "nombres", nullable = false, length = 100)
	private String nombres;

	@Column(name = "apellidos", nullable = false, length = 100)
	private String apellidos;

	@Column(name = "login", nullable = false, length = 50)
	private String login;

	@Column(name = "password", nullable = false, length = 200)
	private String password;

	@Column(name = "correo", nullable = false, length = 100)
	private String correo;

	@Column(name = "celular", nullable = false, length = 15)
	private String celular;

	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_tipo_doc", nullable = false)
	private TipoDocumento tipodocumento;

	@Column(name = "num_doc", nullable = false, length = 15)
	private String numDoc;

	@Column(name = "estado", nullable = false)
	private int estado;

	@Column(name = "foto", length = 200)
	private String foto;

	public String getNombreCompleto() {
		if (nombres != null && apellidos != null) {
			return nombres.concat(" ").concat(apellidos);
		} else {
			return "";
		}
	}

	// Constructor adicional para inicializar solo con el ID
	public Usuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	@Column(name = "fecha_registro", nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private LocalDateTime fechaRegistro;

	@Column(name = "fecha_actualizacion", nullable = true)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private LocalDateTime fechaActualizacion;
	
	

}
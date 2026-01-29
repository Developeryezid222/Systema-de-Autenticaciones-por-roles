package pe.edu.cibertec.ciberpass.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tipo_documento")
public class TipoDocumento {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_tipo_doc")
	private int idTipoDoc;

	@Column(name = "descripcion", nullable = false, length = 100)
	private String descripcion;

	// Constructor adicional para inicializar solo con el ID
	public TipoDocumento(int idTipoDoc) {
		this.idTipoDoc = idTipoDoc;
	}
}


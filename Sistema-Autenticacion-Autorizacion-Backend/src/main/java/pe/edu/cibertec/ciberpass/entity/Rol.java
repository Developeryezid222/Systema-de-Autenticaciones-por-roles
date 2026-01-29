package pe.edu.cibertec.ciberpass.entity;

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
@Table(name = "rol")
public class Rol {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idRol;
	private String nombre;
	private int estado;
	
	public Rol(int idRol) {
        this.idRol = idRol;
    }

}

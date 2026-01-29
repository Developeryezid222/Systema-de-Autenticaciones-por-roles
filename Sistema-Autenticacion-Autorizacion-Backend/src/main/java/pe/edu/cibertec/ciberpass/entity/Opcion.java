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
@Table(name = "opcion")
public class Opcion {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_opcion")
    private int idOpcion;

    @Column(name = "nombre", length = 255, nullable = true)
    private String nombre;

    @Column(name = "ruta", length = 255, nullable = true)
    private String ruta;

    @Column(name = "estado", nullable = false)
    private int estado;

    @Column(name = "tipo", nullable = false)
    private int tipo; 

}


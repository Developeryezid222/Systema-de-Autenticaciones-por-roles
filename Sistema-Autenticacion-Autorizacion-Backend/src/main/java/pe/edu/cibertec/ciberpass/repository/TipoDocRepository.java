package pe.edu.cibertec.ciberpass.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.edu.cibertec.ciberpass.entity.TipoDocumento;

@Repository
public interface TipoDocRepository extends JpaRepository<TipoDocumento, Integer>{

}

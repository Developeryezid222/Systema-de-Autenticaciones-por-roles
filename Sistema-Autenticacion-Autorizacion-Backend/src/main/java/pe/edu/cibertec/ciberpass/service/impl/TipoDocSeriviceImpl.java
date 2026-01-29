package pe.edu.cibertec.ciberpass.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.edu.cibertec.ciberpass.entity.TipoDocumento;
import pe.edu.cibertec.ciberpass.repository.TipoDocRepository;
import pe.edu.cibertec.ciberpass.service.TipoDocService;

@Service
public class TipoDocSeriviceImpl implements TipoDocService{
	
	@Autowired
    private TipoDocRepository tipoDocumentoRepository; 

	@Override
	public List<TipoDocumento> listaTodos() {
		// TODO Auto-generated method stub
		return tipoDocumentoRepository.findAll();
	}

}

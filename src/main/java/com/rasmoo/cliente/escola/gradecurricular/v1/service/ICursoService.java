package com.rasmoo.cliente.escola.gradecurricular.v1.service;

import java.util.List;

import com.rasmoo.cliente.escola.gradecurricular.entity.CursoEntity;
import com.rasmoo.cliente.escola.gradecurricular.v1.model.CursoModel;

public interface ICursoService {
	
	Boolean cadastrar(CursoModel cursoModel);
	
	Boolean atualizar(CursoModel cursoModel);
	
	Boolean excluir(Long cursoId);
	
	CursoEntity consultarPorCodigo(String codCurso);
	
	List<CursoEntity> listar();

}

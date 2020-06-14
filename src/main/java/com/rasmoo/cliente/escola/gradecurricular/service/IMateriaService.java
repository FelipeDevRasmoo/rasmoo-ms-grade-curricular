package com.rasmoo.cliente.escola.gradecurricular.service;

import java.util.List;

import com.rasmoo.cliente.escola.gradecurricular.dto.MateriaDto;
import com.rasmoo.cliente.escola.gradecurricular.entity.MateriaEntity;

public interface IMateriaService {
	
	public Boolean atualizar(final MateriaEntity materia);
	
	public Boolean excluir(final Long id);
	
	/*
	 * LISTAR todas matérias. 
	 */
	public List<MateriaEntity> listar();
	
	/*
	 * CONSULTA uma matéria a partir do ID.  
	 */
	public MateriaEntity consultar(final Long id);
	
	/*
	 * CADASTRAR uma matéria.  
	 */
	public Boolean cadastrar(final MateriaDto materia);
}

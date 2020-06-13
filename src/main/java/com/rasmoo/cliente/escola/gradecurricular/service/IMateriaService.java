package com.rasmoo.cliente.escola.gradecurricular.service;

import com.rasmoo.cliente.escola.gradecurricular.entity.MateriaEntity;

public interface IMateriaService {
	
	public Boolean atualizar(final MateriaEntity materia);
	
	public Boolean excluir(final Long id);

}

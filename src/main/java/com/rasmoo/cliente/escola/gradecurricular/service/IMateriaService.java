package com.rasmoo.cliente.escola.gradecurricular.service;

import java.util.List;

import com.rasmoo.cliente.escola.gradecurricular.dto.MateriaDto;

public interface IMateriaService {
	
	public Boolean atualizar(final MateriaDto materia);
	
	public Boolean excluir(final Long id);
	
	/*
	 * LISTAR todas matérias. 
	 */
	public List<MateriaDto> listar();
	
	/*
	 * CONSULTA uma matéria a partir do ID.  
	 */
	public MateriaDto consultar(final Long id);
	
	/*
	 * CADASTRAR uma matéria.  
	 */
	public Boolean cadastrar(final MateriaDto materia);
	
	public List<MateriaDto> listarPorHorarioMinimo(int horaMinima);
	
	public List<MateriaDto> listarPorFrequencia(int frequencia);
	
}

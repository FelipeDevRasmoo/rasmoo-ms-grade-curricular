package com.rasmoo.cliente.escola.gradecurricular.service.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rasmoo.cliente.escola.gradecurricular.dto.MateriaDto;
import com.rasmoo.cliente.escola.gradecurricular.entity.MateriaEntity;
import com.rasmoo.cliente.escola.gradecurricular.repository.IMateriaRepository;
import com.rasmoo.cliente.escola.gradecurricular.service.MateriaService;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class MateriaServiceUnitTest {

	@Mock
	private IMateriaRepository materiaRepository;

	@InjectMocks
	private MateriaService materiaService;

	private static MateriaEntity materiaEntity;

	@BeforeAll
	public static void init() {
		materiaEntity = new MateriaEntity();
		materiaEntity.setId(1L);
		materiaEntity.setCodigo("ILP");
		materiaEntity.setFrequencia(2);
		materiaEntity.setHoras(34);
		materiaEntity.setNome("INTRODUCAO A LINGUAGEM DE PROGRAMCAO");
	}

	/*
	 * 
	 * CENARIOS DE SUCESSO
	 * 
	 */

	@Test
	public void testConsultarSuccesso() {
		Mockito.when(this.materiaRepository.findById(1L)).thenReturn(Optional.of(materiaEntity));

		MateriaDto materiaDto = this.materiaService.consultar(1L);

		assertNotNull(materiaDto);
		assertEquals("ILP", materiaDto.getCodigo());
		assertEquals(1, materiaDto.getId());
		assertEquals(2, materiaDto.getFrequencia());

		Mockito.verify(this.materiaRepository, times(1)).findById(1L);
	}

	@Test
	public void testListarSuccesso() {
		List<MateriaEntity> listMateria = new ArrayList<>();
		listMateria.add(materiaEntity);
		Mockito.when(this.materiaRepository.findAll()).thenReturn(listMateria);

		List<MateriaDto> listMateriaDto = this.materiaService.listar();

		assertNotNull(listMateriaDto);
		assertEquals("ILP", listMateriaDto.get(0).getCodigo());
		assertEquals(1, listMateriaDto.get(0).getId());
		assertEquals("/materia/1", listMateriaDto.get(0).getLinks().getRequiredLink("self").getHref());
		assertEquals(1, listMateriaDto.size());

		Mockito.verify(this.materiaRepository, times(1)).findAll();
	}

	@Test
	public void testListarPorHorarioMinimoSuccesso() {
		List<MateriaEntity> listMateria = new ArrayList<>();
		listMateria.add(materiaEntity);
		Mockito.when(this.materiaRepository.findByHoraMinima(64)).thenReturn(listMateria);

		List<MateriaDto> listMateriaDto = this.materiaService.listarPorHorarioMinimo(64);

		assertNotNull(listMateriaDto);
		assertEquals("ILP", listMateriaDto.get(0).getCodigo());
		assertEquals(1, listMateriaDto.get(0).getId());
		assertEquals(1, listMateriaDto.size());

		Mockito.verify(this.materiaRepository, times(1)).findByHoraMinima(64);
	}

	@Test
	public void testListarPorFrequenciaSuccesso() {
		List<MateriaEntity> listMateria = new ArrayList<>();
		listMateria.add(materiaEntity);
		Mockito.when(this.materiaRepository.findByFrequencia(2)).thenReturn(listMateria);

		List<MateriaDto> listMateriaDto = this.materiaService.listarPorFrequencia(2);

		assertNotNull(listMateriaDto);
		assertEquals("ILP", listMateriaDto.get(0).getCodigo());
		assertEquals(1, listMateriaDto.get(0).getId());
		assertEquals(1, listMateriaDto.size());

		Mockito.verify(this.materiaRepository, times(1)).findByFrequencia(2);
	}

	@Test
	public void testCadastrarSuccesso() {

		MateriaDto materiaDto = new MateriaDto();
		materiaDto.setCodigo("ILP");
		materiaDto.setFrequencia(2);
		materiaDto.setHoras(34);
		materiaDto.setNome("INTRODUCAO A LINGUAGEM DE PROGRAMCAO");

		materiaEntity.setId(null);

		Mockito.when(this.materiaRepository.findByCodigo("ILP")).thenReturn(null);
		Mockito.when(this.materiaRepository.save(materiaEntity)).thenReturn(materiaEntity);

		Boolean sucessoCadastro = this.materiaService.cadastrar(materiaDto);

		assertTrue(sucessoCadastro);

		Mockito.verify(this.materiaRepository, times(1)).save(materiaEntity);
		Mockito.verify(this.materiaRepository, times(1)).findByCodigo("ILP");

		materiaEntity.setId(1L);
	}

	@Test
	public void testAtualizarSuccesso() {

		MateriaDto materiaDto = new MateriaDto();
		materiaDto.setId(1L);
		materiaDto.setCodigo("ILP");
		materiaDto.setFrequencia(2);
		materiaDto.setHoras(34);
		materiaDto.setNome("INTRODUCAO A LINGUAGEM DE PROGRAMCAO");

		Mockito.when(this.materiaRepository.findById(1L)).thenReturn(Optional.of(materiaEntity));
		Mockito.when(this.materiaRepository.save(materiaEntity)).thenReturn(materiaEntity);

		Boolean sucessoAtualizacao = this.materiaService.atualizar(materiaDto);

		assertTrue(sucessoAtualizacao);

		Mockito.verify(this.materiaRepository, times(1)).save(materiaEntity);
		Mockito.verify(this.materiaRepository, times(1)).findById(1L);
	}

	@Test
	public void testExcluirSuccesso() {

		Mockito.when(this.materiaRepository.findById(1L)).thenReturn(Optional.of(materiaEntity));

		Boolean sucessoCadastro = this.materiaService.excluir(1L);

		assertTrue(sucessoCadastro);

		Mockito.verify(this.materiaRepository, times(1)).deleteById(1L);
		Mockito.verify(this.materiaRepository, times(1)).findById(1L);

	}

	/*
	 * 
	 * CENARIOS DE THROW-MATERIA-EXCEPTION
	 * 
	 */

	/*
	 * 
	 * CENARIOS DE THROW EXCEPTION
	 * 
	 */

}

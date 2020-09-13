package com.rasmoo.cliente.escola.gradecurricular.service.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
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
import org.springframework.http.HttpStatus;

import com.rasmoo.cliente.escola.gradecurricular.entity.CursoEntity;
import com.rasmoo.cliente.escola.gradecurricular.entity.MateriaEntity;
import com.rasmoo.cliente.escola.gradecurricular.repository.ICursoRepository;
import com.rasmoo.cliente.escola.gradecurricular.repository.IMateriaRepository;
import com.rasmoo.cliente.escola.gradecurricular.v1.constant.MensagensConstant;
import com.rasmoo.cliente.escola.gradecurricular.v1.exception.CursoException;
import com.rasmoo.cliente.escola.gradecurricular.v1.model.CursoModel;
import com.rasmoo.cliente.escola.gradecurricular.v1.service.CursoService;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class CursoServiceUnitTest {

	@Mock
	private IMateriaRepository materiaRepository;

	@Mock
	private ICursoRepository cursoRepository;

	@InjectMocks
	private CursoService cursoService;

	private static MateriaEntity materiaEntity;

	private static CursoEntity cursoEntity;

	@BeforeAll
	public static void init() {

		materiaEntity = new MateriaEntity();
		materiaEntity.setId(1L);
		materiaEntity.setCodigo("ILP");
		materiaEntity.setFrequencia(1);
		materiaEntity.setHoras(64);
		materiaEntity.setNome("INTRODUCAO A LINGUAGEM DE PROGRAMACAO");

		List<MateriaEntity> listMat = new ArrayList<>();
		listMat.add(materiaEntity);

		cursoEntity = new CursoEntity();
		cursoEntity.setId(1L);
		cursoEntity.setCodigo("CC");
		cursoEntity.setNome("Ciencias da computacao");
		cursoEntity.setMaterias(listMat);
	}

	/*
	 * 
	 * CENARIOS DE SUCESSO
	 * 
	 */

	@Test
	public void testListarSucesso() {
		List<CursoEntity> listCurso = new ArrayList<>();
		listCurso.add(cursoEntity);

		Mockito.when(this.cursoRepository.findAll()).thenReturn(listCurso);

		List<CursoEntity> listCursoEntity = this.cursoService.listar();

		assertNotNull(listCursoEntity);
		assertEquals("CC", listCursoEntity.get(0).getCodigo());
		assertEquals(1, listCursoEntity.get(0).getId());
		assertEquals(1, listCursoEntity.size());

		Mockito.verify(this.cursoRepository, times(1)).findAll();

	}

	@Test
	public void testConsultarSucesso() {
		Mockito.when(this.cursoRepository.findCursoByCodigo("CC")).thenReturn(cursoEntity);
		CursoEntity cursoEntity = this.cursoService.consultarPorCodigo("CC");

		assertNotNull(cursoEntity);
		assertEquals("CC", cursoEntity.getCodigo());
		assertEquals(1, cursoEntity.getId());

		Mockito.verify(this.cursoRepository, times(1)).findCursoByCodigo("CC");
	}

	@Test
	public void testCadastrarSucesso() {
		List<Long> listId = new ArrayList<Long>();
		listId.add(1L);

		CursoEntity novoCursoEntity = new CursoEntity();
		novoCursoEntity.setCodigo("CC");
		novoCursoEntity.setMaterias(new ArrayList<>());
		novoCursoEntity.setNome("ENGENHARIA DA COMPUTACAO");

		CursoModel cursoModel = new CursoModel();
		cursoModel.setCodCurso("CC");
		cursoModel.setNome("ENGENHARIA DA COMPUTACAO");
		cursoModel.setMaterias(listId);

		Mockito.when(this.cursoRepository.findCursoByCodigo("CC")).thenReturn(null);
		Mockito.when(this.materiaRepository.findById(1L)).thenReturn(Optional.empty());
		Mockito.when(this.cursoRepository.save(novoCursoEntity)).thenReturn(novoCursoEntity);

		Boolean sucesso = this.cursoService.cadastrar(cursoModel);

		assertTrue(sucesso);

		Mockito.verify(this.cursoRepository, times(1)).findCursoByCodigo("CC");
		Mockito.verify(this.cursoRepository, times(1)).save(novoCursoEntity);
		Mockito.verify(this.materiaRepository, times(1)).findById(1L);

	}

	@Test
	public void testAtualizarSucesso() {
		List<Long> listId = new ArrayList<Long>();
		listId.add(1L);

		CursoModel cursoModel = new CursoModel();
		cursoModel.setId(1L);
		cursoModel.setCodCurso("CC");
		cursoModel.setNome("Ciencias da computacao");
		cursoModel.setMaterias(listId);

		Mockito.when(this.cursoRepository.findCursoByCodigo("CC")).thenReturn(cursoEntity);
		Mockito.when(this.materiaRepository.findById(1L)).thenReturn(Optional.of(materiaEntity));
		Mockito.when(this.cursoRepository.save(cursoEntity)).thenReturn(cursoEntity);

		Boolean sucesso = this.cursoService.atualizar(cursoModel);

		assertTrue(sucesso);

		Mockito.verify(this.cursoRepository, times(1)).findCursoByCodigo("CC");
		Mockito.verify(this.cursoRepository, times(1)).save(cursoEntity);
		Mockito.verify(this.materiaRepository, times(2)).findById(1L);

	}

	@Test
	public void testExcluirSucesso() {

		Mockito.when(this.cursoRepository.findById(1L)).thenReturn(Optional.of(cursoEntity));
		Boolean sucesso = this.cursoService.excluir(1L);

		assertTrue(sucesso);

		Mockito.verify(this.cursoRepository, times(1)).findById(1L);
		Mockito.verify(this.cursoRepository, times(1)).deleteById(1L);
	}

	/*
	 * 
	 * CENARIOS DE THROW-CURSO-EXCEPTION
	 * 
	 */

	@Test
	public void testAtualizarThrowCursoException() {

		List<Long> listId = new ArrayList<Long>();
		listId.add(1L);

		CursoModel cursoModel = new CursoModel();
		cursoModel.setId(1L);
		cursoModel.setCodCurso("CC");
		cursoModel.setNome("Ciencias da computacao");
		cursoModel.setMaterias(listId);

		Mockito.when(this.cursoRepository.findCursoByCodigo("CC")).thenReturn(null);

		CursoException cursoException;

		cursoException = assertThrows(CursoException.class, () -> {
			this.cursoService.atualizar(cursoModel);
		});

		assertEquals(HttpStatus.NOT_FOUND, cursoException.getHttpStatus());
		assertEquals(MensagensConstant.ERRO_CURSO_NAO_ENCONTRADO.getValor(), cursoException.getMessage());

		Mockito.verify(this.cursoRepository, times(1)).findCursoByCodigo("CC");
		Mockito.verify(this.cursoRepository, times(0)).save(cursoEntity);

	}

	@Test
	public void testExcluirThrowCursoException() {

		Mockito.when(this.cursoRepository.findById(1L)).thenReturn(Optional.empty());

		CursoException cursoException;

		cursoException = assertThrows(CursoException.class, () -> {
			this.cursoService.excluir(1L);
		});

		assertEquals(HttpStatus.NOT_FOUND, cursoException.getHttpStatus());
		assertEquals(MensagensConstant.ERRO_CURSO_NAO_ENCONTRADO.getValor(), cursoException.getMessage());

		Mockito.verify(this.cursoRepository, times(1)).findById(1L);
		Mockito.verify(this.cursoRepository, times(0)).deleteById(1L);

	}

	@Test
	public void testCadastrarComIdThrowMateriaException() {

		CursoModel cursoModel = new CursoModel();
		cursoModel.setId(1L);
		cursoModel.setCodCurso("CC");
		cursoModel.setNome("Ciencias da computacao");

		CursoException cursoException;

		cursoException = assertThrows(CursoException.class, () -> {
			this.cursoService.cadastrar(cursoModel);
		});

		assertEquals(HttpStatus.BAD_REQUEST, cursoException.getHttpStatus());
		assertEquals(MensagensConstant.ERRO_ID_INFORMADO.getValor(), cursoException.getMessage());

		Mockito.verify(this.cursoRepository, times(0)).findCursoByCodigo("CC");
		Mockito.verify(this.cursoRepository, times(0)).save(cursoEntity);

	}

	@Test
	public void testCadastrarComCodigoExistenteThrowMateriaException() {

		CursoModel cursoModel = new CursoModel();
		cursoModel.setCodCurso("CC");
		cursoModel.setNome("Ciencias da computacao");

		Mockito.when(this.cursoRepository.findCursoByCodigo("CC")).thenReturn(cursoEntity);

		CursoException cursoException;

		cursoException = assertThrows(CursoException.class, () -> {
			this.cursoService.cadastrar(cursoModel);
		});

		assertEquals(HttpStatus.BAD_REQUEST, cursoException.getHttpStatus());
		assertEquals(MensagensConstant.ERRO_CURSO_CADASTRADO_ANTERIORMENTE.getValor(), cursoException.getMessage());

		Mockito.verify(this.cursoRepository, times(1)).findCursoByCodigo("CC");
		Mockito.verify(this.cursoRepository, times(0)).save(cursoEntity);

	}

	/*
	 * 
	 * CENARIOS DE THROW EXCEPTION
	 * 
	 */
	
	@Test
	public void testAtualizarThrowException() {

		List<Long> listId = new ArrayList<Long>();
		listId.add(1L);

		CursoModel cursoModel = new CursoModel();
		cursoModel.setId(1L);
		cursoModel.setCodCurso("CC");
		cursoModel.setNome("Ciencias da computacao");
		cursoModel.setMaterias(listId);

		Mockito.when(this.cursoRepository.findCursoByCodigo("CC")).thenReturn(cursoEntity);
		Mockito.when(this.materiaRepository.findById(1L)).thenReturn(Optional.of(materiaEntity));
		Mockito.when(this.cursoRepository.save(cursoEntity)).thenThrow(IllegalStateException.class);
		
		CursoException cursoException;

		cursoException = assertThrows(CursoException.class, () -> {
			this.cursoService.atualizar(cursoModel);
		});

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, cursoException.getHttpStatus());
		assertEquals(MensagensConstant.ERRO_GENERICO.getValor(), cursoException.getMessage());

		Mockito.verify(this.cursoRepository, times(1)).findCursoByCodigo("CC");
		Mockito.verify(this.materiaRepository, times(2)).findById(1L);
		Mockito.verify(this.cursoRepository, times(1)).save(cursoEntity);

	}
	
	@Test
	public void testCadastrarThrowException() {
		
		List<Long> listId = new ArrayList<Long>();
		listId.add(1L);

		CursoModel cursoModel = new CursoModel();
		cursoModel.setCodCurso("CC");
		cursoModel.setNome("Ciencias da computacao");
		cursoModel.setMaterias(listId);
		
		CursoEntity novoCursoEntity = new CursoEntity();
		novoCursoEntity.setCodigo("CC");
		novoCursoEntity.setMaterias(new ArrayList<>());
		novoCursoEntity.setNome("Ciencias da computacao");

		Mockito.when(this.cursoRepository.findCursoByCodigo("CC")).thenReturn(null);
		Mockito.when(this.materiaRepository.findById(1L)).thenReturn(Optional.empty());
		Mockito.when(this.cursoRepository.save(novoCursoEntity)).thenThrow(IllegalStateException.class);
		
		CursoException cursoException;

		cursoException = assertThrows(CursoException.class, () -> {
			this.cursoService.cadastrar(cursoModel);
		});

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, cursoException.getHttpStatus());
		assertEquals(MensagensConstant.ERRO_GENERICO.getValor(), cursoException.getMessage());

		Mockito.verify(this.cursoRepository, times(1)).findCursoByCodigo("CC");
		Mockito.verify(this.materiaRepository, times(1)).findById(1L);
		Mockito.verify(this.cursoRepository, times(1)).save(novoCursoEntity);
		
	}
	
	@Test
	public void testConsultarThrowException() {
		
		Mockito.when(this.cursoRepository.findCursoByCodigo("CC")).thenThrow(IllegalStateException.class);
		
		CursoException cursoException;
		
		cursoException = assertThrows(CursoException.class, ()->{
			 this.cursoService.consultarPorCodigo("CC");
		});
		
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, cursoException.getHttpStatus());
		assertEquals(MensagensConstant.ERRO_GENERICO.getValor(), cursoException.getMessage());
		
		Mockito.verify(this.cursoRepository, times(1)).findCursoByCodigo("CC");
		
	}
	
	@Test
	public void testListarThrowException() {
		
		Mockito.when(this.cursoRepository.findAll()).thenThrow(IllegalStateException.class);
		
		CursoException cursoException;
		
		cursoException = assertThrows(CursoException.class, ()->{
			 this.cursoService.listar();
		});
		
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, cursoException.getHttpStatus());
		assertEquals(MensagensConstant.ERRO_GENERICO.getValor(), cursoException.getMessage());
		
		Mockito.verify(this.cursoRepository, times(1)).findAll();
		
	}
	
	@Test
	public void testExcluirThrowException() {
		
		Mockito.when(this.cursoRepository.findById(1L)).thenReturn(Optional.of(cursoEntity));
		Mockito.doThrow(IllegalStateException.class).when(this.cursoRepository).deleteById(1L);
		
		CursoException cursoException;
		
		cursoException = assertThrows(CursoException.class, ()->{
			 this.cursoService.excluir(1L);
		});
		
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, cursoException.getHttpStatus());
		assertEquals(MensagensConstant.ERRO_GENERICO.getValor(), cursoException.getMessage());
		
		Mockito.verify(this.cursoRepository, times(1)).findById(1L);
		Mockito.verify(this.cursoRepository, times(1)).deleteById(1L);
		
	}

}

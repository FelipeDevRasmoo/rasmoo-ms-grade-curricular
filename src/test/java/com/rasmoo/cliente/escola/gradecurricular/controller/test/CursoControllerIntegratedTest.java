//package com.rasmoo.cliente.escola.gradecurricular.controller.test;
//
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertTrue;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//import java.util.Arrays;
//import java.util.List;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.platform.runner.JUnitPlatform;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.boot.web.server.LocalServerPort;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//
//import com.rasmoo.cliente.escola.gradecurricular.dto.MateriaDto;
//import com.rasmoo.cliente.escola.gradecurricular.entity.CursoEntity;
//import com.rasmoo.cliente.escola.gradecurricular.entity.MateriaEntity;
//import com.rasmoo.cliente.escola.gradecurricular.model.Response;
//import com.rasmoo.cliente.escola.gradecurricular.repository.ICursoRepository;
//import com.rasmoo.cliente.escola.gradecurricular.repository.IMateriaRepository;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@RunWith(JUnitPlatform.class)
//public class CursoControllerIntegratedTest {
//
//	@LocalServerPort
//	private int port;
//
//	@Autowired
//	private TestRestTemplate restTemplate;
//
//	@Autowired
//	private IMateriaRepository materiaRepository;
//
//	@Autowired
//	private ICursoRepository cursoRepository;
//
//	@BeforeEach
//	public void init() {
//		this.montaMateriaBaseDeDados();
//		this.montaCursoBaseDeDados();
//	}
//
//	@AfterEach
//	public void finish() {
//		this.cursoRepository.deleteAll();
//		this.materiaRepository.deleteAll();
//	}
//
//	private String montaUri(String urn) {
//		return "http://localhost:" + this.port + "/curso/" + urn;
//	}
//
//	private void montaMateriaBaseDeDados() {
//		MateriaEntity m1 = new MateriaEntity();
//		m1.setCodigo("ILP");
//		m1.setFrequencia(2);
//		m1.setHoras(64);
//		m1.setNome("INTRODUCAO A LINGUAGEM DE PROGRAMACAO");
//
//		MateriaEntity m2 = new MateriaEntity();
//		m2.setCodigo("POO");
//		m2.setFrequencia(2);
//		m2.setHoras(84);
//		m2.setNome("PROGRAMACAO ORIENTADA A OBJETOS");
//
//		MateriaEntity m3 = new MateriaEntity();
//		m3.setCodigo("APA");
//		m3.setFrequencia(1);
//		m3.setHoras(102);
//		m3.setNome("ANALISE E PROJETOS DE ALGORITMOS");
//
//		this.materiaRepository.saveAll(Arrays.asList(m1, m2, m3));
//	}
//
//	private void montaCursoBaseDeDados() {
//
//		List<MateriaEntity> listMaterias = this.materiaRepository.findAll();
//
//		CursoEntity c1 = new CursoEntity();
//		c1.setCodigo("ENGC");
//		c1.setNome("ENGENHARIA DA COMPUTACAO");
//		c1.setMaterias(listMaterias);
//
//		this.cursoRepository.save(c1);
//	}
//
//	/*
//	 * 
//	 * **********************CENÁRIOS DE SUCESSO************************
//	 * 
//	 */
//
//	@Test
//	public void testListarCursos() {
//
//		ResponseEntity<Response<List<CursoEntity>>> cursos = restTemplate.exchange(this.montaUri(""), HttpMethod.GET,
//				null, new ParameterizedTypeReference<Response<List<CursoEntity>>>() {
//				});
//		assertNotNull(cursos.getBody().getData());
//		assertEquals(1, cursos.getBody().getData().size());
//		assertEquals(200, cursos.getBody().getStatusCode());
//	}
//
//	@Test
//	public void testConsultarCursoPorCodigo() {
//
//		ResponseEntity<Response<CursoEntity>> curso = restTemplate.exchange(
//				this.montaUri("/ENGC"), HttpMethod.GET, null,
//				new ParameterizedTypeReference<Response<CursoEntity>>() {
//				});
//		assertNotNull(curso.getBody().getData());
//		assertEquals("ENGENHARIA DA COMPUTACAO", curso.getBody().getData().getNome());
//		assertEquals(200, curso.getBody().getStatusCode());
//	}
//
//	@Test
//	public void testAtualizarCurso() {
//
//		List<CursoEntity> cursosList = this.cursoRepository.findAll();
//		CursoEntity cursoEntity = cursosList.get(0);
//
//		cursoEntity.setNome("Teste Atualiza curso");
//
//		HttpEntity<CursoEntity> request = new HttpEntity<>(cursoEntity);
//
//		ResponseEntity<Response<Boolean>> curso = restTemplate.exchange(
//				this.montaUri("/"), HttpMethod.PUT, request,
//				new ParameterizedTypeReference<Response<Boolean>>() {
//				});
//		CursoEntity materiaAtualizada = this.cursoRepository.findCursoByCodigo(cursoEntity.getCodigo());
//
//		assertTrue(curso.getBody().getData());
//		assertEquals("Teste Atualiza curso", materiaAtualizada.getNome());
//		assertEquals(200, curso.getBody().getStatusCode());
//	}
//
//	@Test
//	public void testCadastrarCurso() {
//
//		MateriaEntity m4 = new MateriaEntity();
//		m4.setCodigo("CALC1");
//		m4.setFrequencia(2);
//		m4.setHoras(102);
//		m4.setNome("CALCULO I");
//
//		HttpEntity<MateriaEntity> request = new HttpEntity<>(m4);
//
//		ResponseEntity<Response<Boolean>> materias = restTemplate.exchange(
//				"http://localhost:" + this.port + "/materia/", HttpMethod.POST, request,
//				new ParameterizedTypeReference<Response<Boolean>>() {
//				});
//		List<MateriaEntity> listMateriaAtualizada = this.materiaRepository.findAll();
//
//		assertTrue(materias.getBody().getData());
//		assertEquals(4, listMateriaAtualizada.size());
//		assertEquals(201, materias.getBody().getStatusCode());
//	}
//
//	@Test
//	public void testExcluirCursoPorId() {
//
//		List<CursoEntity> List = this.materiaRepository.findAll();
//		Long id = materiasList.get(0).getId();
//
//		ResponseEntity<Response<Boolean>> materias = restTemplate.exchange(
//				"http://localhost:" + this.port + "/materia/" + id, HttpMethod.DELETE, null,
//				new ParameterizedTypeReference<Response<Boolean>>() {
//				});
//
//		List<MateriaEntity> listMateriaAtualizada = this.materiaRepository.findAll();
//
//		assertTrue(materias.getBody().getData());
//		assertEquals(2, listMateriaAtualizada.size());
//		assertEquals(200, materias.getBody().getStatusCode());
//	}
//
//}

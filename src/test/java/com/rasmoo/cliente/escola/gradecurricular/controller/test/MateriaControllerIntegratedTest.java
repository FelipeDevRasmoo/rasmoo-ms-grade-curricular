package com.rasmoo.cliente.escola.gradecurricular.controller.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.rasmoo.cliente.escola.gradecurricular.dto.MateriaDto;
import com.rasmoo.cliente.escola.gradecurricular.entity.MateriaEntity;
import com.rasmoo.cliente.escola.gradecurricular.model.Response;
import com.rasmoo.cliente.escola.gradecurricular.repository.IMateriaRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(JUnitPlatform.class)
public class MateriaControllerIntegratedTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private IMateriaRepository materiaRepository;

	private static final String USER = "rasmoo";
	private static final String PASSWORD = "msgradecurricular";

	@BeforeEach
	public void init() {
		this.montaBaseDeDados();
	}

	@AfterEach
	public void finish() {
		this.materiaRepository.deleteAll();
	}

	private void montaBaseDeDados() {
		MateriaEntity m1 = new MateriaEntity();
		m1.setCodigo("ILP");
		m1.setFrequencia(2);
		m1.setHoras(64);
		m1.setNome("INTRODUCAO A LINGUAGEM DE PROGRAMACAO");

		MateriaEntity m2 = new MateriaEntity();
		m2.setCodigo("POO");
		m2.setFrequencia(2);
		m2.setHoras(84);
		m2.setNome("PROGRAMACAO ORIENTADA A OBJETOS");

		MateriaEntity m3 = new MateriaEntity();
		m3.setCodigo("APA");
		m3.setFrequencia(1);
		m3.setHoras(102);
		m3.setNome("ANALISE E PROJETOS DE ALGORITMOS");

		this.materiaRepository.saveAll(Arrays.asList(m1, m2, m3));
	}

	/*
	 * 
	 * **********************CENÁRIOS DE SUCESSO************************
	 * 
	 */

	@Test
	public void testListarMaterias() {

		ResponseEntity<Response<List<MateriaDto>>> materias = restTemplate.withBasicAuth(USER, PASSWORD).exchange(
				"http://localhost:" + this.port + "/materia/", HttpMethod.GET, null,
				new ParameterizedTypeReference<Response<List<MateriaDto>>>() {
				});
		assertNotNull(materias.getBody().getData());
		assertEquals(3, materias.getBody().getData().size());
		assertEquals(200, materias.getBody().getStatusCode());
	}

	@Test
	public void testConsultarMateriasPorHoraMinima() {

		ResponseEntity<Response<List<MateriaDto>>> materias = restTemplate.withBasicAuth(USER, PASSWORD).exchange(
				"http://localhost:" + this.port + "/materia/horario-minimo/80", HttpMethod.GET, null,
				new ParameterizedTypeReference<Response<List<MateriaDto>>>() {
				});
		assertNotNull(materias.getBody().getData());
		assertEquals(2, materias.getBody().getData().size());
		assertEquals(200, materias.getBody().getStatusCode());
	}

	@Test
	public void testConsultarMateriasPorFrequencia() {

		ResponseEntity<Response<List<MateriaDto>>> materias = restTemplate.withBasicAuth(USER, PASSWORD).exchange(
				"http://localhost:" + this.port + "/materia/frequencia/1", HttpMethod.GET, null,
				new ParameterizedTypeReference<Response<List<MateriaDto>>>() {
				});
		assertNotNull(materias.getBody().getData());
		assertEquals(1, materias.getBody().getData().size());
		assertEquals(200, materias.getBody().getStatusCode());
	}

	@Test
	public void testConsultarMateriaPorId() {

		List<MateriaEntity> materiasList = this.materiaRepository.findAll();
		Long id = materiasList.get(0).getId();

		ResponseEntity<Response<MateriaDto>> materias = restTemplate.withBasicAuth(USER, PASSWORD).exchange(
				"http://localhost:" + this.port + "/materia/" + id, HttpMethod.GET, null,
				new ParameterizedTypeReference<Response<MateriaDto>>() {
				});
		assertNotNull(materias.getBody().getData());
		assertEquals(id, materias.getBody().getData().getId());
		assertEquals(64, materias.getBody().getData().getHoras());
		assertEquals("ILP", materias.getBody().getData().getCodigo());
		assertEquals(200, materias.getBody().getStatusCode());
	}

	@Test
	public void testAtualizarMateria() {

		List<MateriaEntity> materiasList = this.materiaRepository.findAll();
		MateriaEntity materia = materiasList.get(0);

		materia.setNome("Teste Atualiza materia");

		HttpEntity<MateriaEntity> request = new HttpEntity<>(materia);

		ResponseEntity<Response<Boolean>> materias = restTemplate.withBasicAuth(USER, PASSWORD).exchange(
				"http://localhost:" + this.port + "/materia/", HttpMethod.PUT, request,
				new ParameterizedTypeReference<Response<Boolean>>() {
				});
		MateriaEntity materiaAtualizada = this.materiaRepository.findById(materia.getId()).get();

		assertTrue(materias.getBody().getData());
		assertEquals("Teste Atualiza materia", materiaAtualizada.getNome());
		assertEquals(200, materias.getBody().getStatusCode());
	}

	@Test
	public void testCadastrarMateria() {

		MateriaEntity m4 = new MateriaEntity();
		m4.setCodigo("CALC1");
		m4.setFrequencia(2);
		m4.setHoras(102);
		m4.setNome("CALCULO I");

		HttpEntity<MateriaEntity> request = new HttpEntity<>(m4);

		ResponseEntity<Response<Boolean>> materias = restTemplate.withBasicAuth(USER, PASSWORD).exchange(
				"http://localhost:" + this.port + "/materia/", HttpMethod.POST, request,
				new ParameterizedTypeReference<Response<Boolean>>() {
				});
		List<MateriaEntity> listMateriaAtualizada = this.materiaRepository.findAll();

		assertTrue(materias.getBody().getData());
		assertEquals(4, listMateriaAtualizada.size());
		assertEquals(201, materias.getBody().getStatusCode());
	}

	@Test
	public void testExcluirMateriaPorId() {

		List<MateriaEntity> materiasList = this.materiaRepository.findAll();
		Long id = materiasList.get(0).getId();

		ResponseEntity<Response<Boolean>> materias = restTemplate.withBasicAuth(USER, PASSWORD).exchange(
				"http://localhost:" + this.port + "/materia/" + id, HttpMethod.DELETE, null,
				new ParameterizedTypeReference<Response<Boolean>>() {
				});

		List<MateriaEntity> listMateriaAtualizada = this.materiaRepository.findAll();

		assertTrue(materias.getBody().getData());
		assertEquals(2, listMateriaAtualizada.size());
		assertEquals(200, materias.getBody().getStatusCode());
	}

}

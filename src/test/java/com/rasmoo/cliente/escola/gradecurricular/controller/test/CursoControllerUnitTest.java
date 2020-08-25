package com.rasmoo.cliente.escola.gradecurricular.controller.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.rasmoo.cliente.escola.gradecurricular.entity.CursoEntity;
import com.rasmoo.cliente.escola.gradecurricular.entity.MateriaEntity;
import com.rasmoo.cliente.escola.gradecurricular.model.CursoModel;
import com.rasmoo.cliente.escola.gradecurricular.model.Response;
import com.rasmoo.cliente.escola.gradecurricular.service.ICursoService;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(JUnitPlatform.class)
public class CursoControllerUnitTest {

	@LocalServerPort
	private int port;

	@MockBean
	private ICursoService cursoService;

	@Autowired
	private TestRestTemplate restTemplate;

	private static CursoModel cursoModel;
	
	private static final String USER = "rasmoo";
	private static final String PASSWORD = "msgradecurricular";

	@BeforeAll
	public static void init() {

		cursoModel = new CursoModel();
		cursoModel.setId(1L);
		cursoModel.setCodCurso("ENGCP");
		cursoModel.setNome("ENGENHARIA DA COMPUTAÇÃO");

	}
	
	private String montaUri(String urn) {
		return "http://localhost:" + this.port + "/curso/"+urn;
	}

	@Test
	public void testListarCursos() {
		Mockito.when(this.cursoService.listar()).thenReturn(new ArrayList<CursoEntity>());

		ResponseEntity<Response<List<CursoEntity>>> cursos = restTemplate.withBasicAuth(USER, PASSWORD).exchange(
				this.montaUri(""), HttpMethod.GET, null,
				new ParameterizedTypeReference<Response<List<CursoEntity>>>() {
				});
		assertNotNull(cursos.getBody().getData());
		assertEquals(200, cursos.getBody().getStatusCode());
	}

	@Test
	public void testConsultarCurso() {
		
		CursoEntity curso = new CursoEntity();
		curso.setId(1L);
		curso.setCodigo("ENGCOMP");
		curso.setNome("ENGENHARIA DA COMPUTACAO");
		curso.setMaterias(new ArrayList<MateriaEntity>());
		Mockito.when(this.cursoService.consultarPorCodigo("ENGCOMP")).thenReturn(curso);

		ResponseEntity<Response<CursoEntity>> cursoResponse = restTemplate.withBasicAuth(USER, PASSWORD).exchange(
				this.montaUri("ENGCOMP"), HttpMethod.GET, null,
				new ParameterizedTypeReference<Response<CursoEntity>>() {
				});
		assertNotNull(cursoResponse.getBody().getData());
		assertEquals(200, cursoResponse.getBody().getStatusCode());
	}

	@Test
	public void testCadastrarCurso() {
		Mockito.when(this.cursoService.cadastrar(cursoModel)).thenReturn(Boolean.TRUE);

		cursoModel.setId(null);
		
		HttpEntity<CursoModel> request = new HttpEntity<>(cursoModel);

		ResponseEntity<Response<Boolean>> curso = restTemplate.withBasicAuth(USER, PASSWORD).exchange(
				this.montaUri(""), HttpMethod.POST, request,
				new ParameterizedTypeReference<Response<Boolean>>() {
				});
		assertTrue(curso.getBody().getData());
		assertEquals(201, curso.getBody().getStatusCode());
		cursoModel.setId(1L);
	}

	@Test
	public void testAtualizarCurso() {
		Mockito.when(this.cursoService.atualizar(cursoModel)).thenReturn(Boolean.TRUE);

		HttpEntity<CursoModel> request = new HttpEntity<>(cursoModel);

		ResponseEntity<Response<Boolean>> curso = restTemplate.withBasicAuth(USER, PASSWORD).exchange(
				this.montaUri(""), HttpMethod.PUT, request,
				new ParameterizedTypeReference<Response<Boolean>>() {
				});
		assertTrue(curso.getBody().getData());
		assertEquals(200, curso.getBody().getStatusCode());
	}

	@Test
	public void testExcluirCurso() {
		Mockito.when(this.cursoService.excluir(1L)).thenReturn(Boolean.TRUE);

		ResponseEntity<Response<Boolean>> curso = restTemplate.withBasicAuth(USER, PASSWORD).exchange(
				this.montaUri("1"), HttpMethod.DELETE, null,
				new ParameterizedTypeReference<Response<Boolean>>() {
				});
		assertTrue(curso.getBody().getData());
		assertEquals(200, curso.getBody().getStatusCode());
	}

}

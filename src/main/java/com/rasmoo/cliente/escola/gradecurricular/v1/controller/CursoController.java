package com.rasmoo.cliente.escola.gradecurricular.v1.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rasmoo.cliente.escola.gradecurricular.config.SwaggerConfig;
import com.rasmoo.cliente.escola.gradecurricular.entity.CursoEntity;
import com.rasmoo.cliente.escola.gradecurricular.v1.model.CursoModel;
import com.rasmoo.cliente.escola.gradecurricular.v1.model.Response;
import com.rasmoo.cliente.escola.gradecurricular.v1.service.ICursoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = SwaggerConfig.CURSO)
@RestController
@RequestMapping("/v1/curso")
public class CursoController {

	@Autowired
	private ICursoService cursoService;

	/*
	 * Cadastro curso, passando a os códigos das matérias a serem cadastradas
	 */
	
	
	@ApiOperation(value = "Cadastrar um novo curso")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Curso criado com sucesso"),
			@ApiResponse(code = 400, message = "Erro na requisição enviada pelo cliente"),
			@ApiResponse(code = 500, message = "Erro interno no serviço"),
	})
	@PostMapping
	public ResponseEntity<Response<Boolean>> cadastrarCurso(@Valid @RequestBody CursoModel curso) {

		Response<Boolean> response = new Response<>();

		response.setData(cursoService.cadastrar(curso));
		response.setStatusCode(HttpStatus.CREATED.value());

		return ResponseEntity.status(HttpStatus.CREATED).body(response);

	}

	/*
	 * Listar cursos
	 */
	@ApiOperation(value = "Listar todos os cursos cadastrados")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lista de cursos exibida com sucesso"),
			@ApiResponse(code = 500, message = "Erro interno no serviço"),
	})
	@GetMapping
	public ResponseEntity<Response<List<CursoEntity>>> listarCurso() {
		Response<List<CursoEntity>> response = new Response<>();
		response.setData(this.cursoService.listar());
		response.setStatusCode(HttpStatus.OK.value());
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	/*
	 * Consultar curso por código do curso
	 */
	@ApiOperation(value = "Consultar curso por código")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Curso encontrado com sucesso"),
			@ApiResponse(code = 404, message = "Curso não encontrado"),
			@ApiResponse(code = 500, message = "Erro interno no serviço"),
	})
	@GetMapping("/{codCurso}")
	public ResponseEntity<Response<CursoEntity>> consultarCursoPorCodigo(@PathVariable String codCurso) {
		Response<CursoEntity> response = new Response<>();
		response.setData(this.cursoService.consultarPorCodigo(codCurso));
		response.setStatusCode(HttpStatus.OK.value());
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	/*
	 * Atualizar curso
	 */
	@ApiOperation(value = "Atualizar um curso")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Curso atualizado com sucesso"),
			@ApiResponse(code = 400, message = "Erro na requisição enviada pelo cliente"),
			@ApiResponse(code = 404, message = "Curso não encontrado"),
			@ApiResponse(code = 500, message = "Erro interno no serviço"),
	})
	@PutMapping
	public ResponseEntity<Response<Boolean>> atualizarCurso(@Valid @RequestBody CursoModel curso) {
		Response<Boolean> response = new Response<>();
		response.setData(cursoService.atualizar(curso));
		response.setStatusCode(HttpStatus.OK.value());

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	/*
	 * Excluir curso
	 */
	@ApiOperation(value = "Excluir um curso")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Curso excluído com sucesso"),
			@ApiResponse(code = 404, message = "Curso não encontrado"),
			@ApiResponse(code = 500, message = "Erro interno no serviço"),
	})
	@DeleteMapping("/{cursoId}")
	public ResponseEntity<Response<Boolean>> excluirCurso( @PathVariable Long cursoId) {
		Response<Boolean> response = new Response<>();
		response.setData(cursoService.excluir(cursoId));
		response.setStatusCode(HttpStatus.OK.value());

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}

package com.rasmoo.cliente.escola.gradecurricular.v1.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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
import com.rasmoo.cliente.escola.gradecurricular.v1.constant.HyperLinkConstant;
import com.rasmoo.cliente.escola.gradecurricular.v1.dto.MateriaDto;
import com.rasmoo.cliente.escola.gradecurricular.v1.model.Response;
import com.rasmoo.cliente.escola.gradecurricular.v1.service.IMateriaService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = SwaggerConfig.MATERIA)
@RestController
@RequestMapping("/v1/materia")
public class MateriaController {

	@Autowired
	private IMateriaService materiaService;

	/*
	 * ALTERACAO NOS METODOS DE CONSULTA
	 */
	
	@ApiOperation(value = "Listar todas as matérias cadastradas")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lista de matérias exibida com sucesso"),
			@ApiResponse(code = 500, message = "Erro interno no serviço"),
	})
	@GetMapping
	public ResponseEntity<Response<List<MateriaDto>>> listarMaterias() {
		Response<List<MateriaDto>> response = new Response<>();
		response.setData(this.materiaService.listar());
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class).listarMaterias())
				.withSelfRel());
		return ResponseEntity.status(HttpStatus.OK).body(response);
	} 
	
	@ApiOperation(value = "Consultar matéria por ID")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Matéria encontrada com sucesso"),
			@ApiResponse(code = 404, message = "Matéria não encontrada"),
			@ApiResponse(code = 500, message = "Erro interno no serviço"),
	})
	@GetMapping("/{id}")
	public ResponseEntity<Response<MateriaDto>> consultarMateria(@PathVariable Long id) {
		Response<MateriaDto> response = new Response<>();
		MateriaDto materia = this.materiaService.consultar(id);
		response.setData(materia);
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class).consultarMateria(id))
				.withSelfRel());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class).excluirMateria(id))
				.withRel(HyperLinkConstant.EXCLUIR.getValor()));
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class).atualizarMateria(materia)).withRel(HyperLinkConstant.ATUALIZAR.getValor()));
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@ApiOperation(value = "Cadastrar uma nova Matéria")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Matéria criada com sucesso"),
			@ApiResponse(code = 400, message = "Erro na requisição enviada pelo cliente"),
			@ApiResponse(code = 500, message = "Erro interno no serviço"),
	})
	@PostMapping
	public ResponseEntity<Response<Boolean>> cadastrarMateria(@Valid @RequestBody MateriaDto materia) {
		Response<Boolean> response = new Response<>();
		response.setData(this.materiaService.cadastrar(materia));
		response.setStatusCode(HttpStatus.CREATED.value());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class).cadastrarMateria(materia))
				.withSelfRel());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class).atualizarMateria(materia))
				.withRel(HyperLinkConstant.ATUALIZAR.getValor()));
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class).listarMaterias())
				.withRel(HyperLinkConstant.LISTAR.getValor()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@ApiOperation(value = "Excluir matéria")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Matéria excluída com sucesso"),
			@ApiResponse(code = 404, message = "Matéria não encontrada"),
			@ApiResponse(code = 500, message = "Erro interno no serviço"),
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Response<Boolean>> excluirMateria(@PathVariable Long id) {
		Response<Boolean> response = new Response<>();
		response.setData(this.materiaService.excluir(id));
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class).excluirMateria(id))
				.withSelfRel());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class).listarMaterias())
				.withRel(HyperLinkConstant.LISTAR.getValor()));
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@ApiOperation(value = "Atualizar matéria")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Matéria atualizada com sucesso"),
			@ApiResponse(code = 400, message = "Erro na requisição enviada pelo cliente"),
			@ApiResponse(code = 404, message = "Matéria não encontrada"),
			@ApiResponse(code = 500, message = "Erro interno no serviço"),
	})
	@PutMapping
	public ResponseEntity<Response<Boolean>> atualizarMateria(@RequestBody MateriaDto materia) {
		Response<Boolean> response = new Response<>();
		response.setData(this.materiaService.atualizar(materia));
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class).atualizarMateria(materia))
				.withSelfRel());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class).listarMaterias())
				.withRel(HyperLinkConstant.LISTAR.getValor()));
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
		
	}
	
	@ApiOperation(value = "Consultar matéria por hora mínima")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Consulta realizada com sucesso"),
			@ApiResponse(code = 404, message = "Matéria não encontrada"),
			@ApiResponse(code = 500, message = "Erro interno no serviço"),
	})
	@GetMapping("/horario-minimo/{horaMinima}")
	public ResponseEntity<Response<List<MateriaDto>>> consultarMateriaPorHoraMinima(@PathVariable int horaMinima) {
		Response<List<MateriaDto>> response = new Response<>();
		List<MateriaDto> materia = this.materiaService.listarPorHorarioMinimo(horaMinima);
		response.setData(materia);
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class).consultarMateriaPorHoraMinima(horaMinima))
				.withSelfRel());
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@ApiOperation(value = "Consultar matéria por frequencia")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Consulta realizada com sucesso"),
			@ApiResponse(code = 404, message = "Matéria não encontrada"),
			@ApiResponse(code = 500, message = "Erro interno no serviço"),
	})
	@GetMapping("/frequencia/{frequencia}")
	public ResponseEntity<Response<List<MateriaDto>>> consultarMateriaPorFrequencia(@PathVariable int frequencia) {
		Response<List<MateriaDto>> response = new Response<>();
		List<MateriaDto> materia = this.materiaService.listarPorFrequencia(frequencia);
		response.setData(materia);
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class).consultarMateriaPorFrequencia(frequencia))
				.withSelfRel());
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}

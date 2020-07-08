package com.rasmoo.cliente.escola.gradecurricular.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rasmoo.cliente.escola.gradecurricular.entity.CursoEntity;
import com.rasmoo.cliente.escola.gradecurricular.entity.MateriaEntity;
import com.rasmoo.cliente.escola.gradecurricular.model.CursoModel;
import com.rasmoo.cliente.escola.gradecurricular.model.Response;
import com.rasmoo.cliente.escola.gradecurricular.repository.ICursoRepository;
import com.rasmoo.cliente.escola.gradecurricular.repository.IMateriaRepository;

@RestController
@RequestMapping("/curso")
public class CursoController {
	
	@Autowired
	private ICursoRepository cursoRepository;
	
	@Autowired
	private IMateriaRepository materiaRepository;
	
	/*
	 * Cadastro curso, passando a os códigos das matérias a serem cadastradas
	 */

	@PostMapping
	public ResponseEntity<Response<Boolean>> cadastrarCurso(@Valid @RequestBody CursoModel curso) {
		
		Response<Boolean> response = new Response<>();
		
		List<MateriaEntity> listMateriaEntity = new ArrayList<>();

		if(!curso.getMaterias().isEmpty()) {
			
			curso.getMaterias().forEach(materia->{
				if(this.materiaRepository.findById(materia).isPresent())
					listMateriaEntity.add(this.materiaRepository.findById(materia).get());
			});
		}

		CursoEntity cursoEntity = new CursoEntity();

		cursoEntity.setCodigo(curso.getCodCurso());
		cursoEntity.setNome(curso.getNome());
		cursoEntity.setMaterias(listMateriaEntity);

		this.cursoRepository.save(cursoEntity);
		
		response.setData(true);
		response.setStatusCode(HttpStatus.OK.value());
		
		return ResponseEntity.status(HttpStatus.OK).body(response);

	}

	/*
	 * Listar cursos
	 */

	@GetMapping
	public ResponseEntity<Response<List<CursoEntity>>> listarCurso() {
		Response<List<CursoEntity>> response = new Response<>();
		response.setData(this.cursoRepository.findAll());
		response.setStatusCode(HttpStatus.OK.value());
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	/*
	 * Consultar curso por código do curso
	 */

	@GetMapping("/{codCurso}")
	public ResponseEntity<Void> consultarCursoPorMateria() {
		throw new NotYetImplementedException();
	}

	/*
	 * Atualizar curso
	 */
	@PutMapping
	public ResponseEntity<Void> atualizarCurso() {
		throw new NotYetImplementedException();
	}

}

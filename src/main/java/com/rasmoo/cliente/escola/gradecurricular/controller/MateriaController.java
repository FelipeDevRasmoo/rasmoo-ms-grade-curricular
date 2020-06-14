package com.rasmoo.cliente.escola.gradecurricular.controller;

import java.util.List;

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

import com.rasmoo.cliente.escola.gradecurricular.entity.MateriaEntity;
import com.rasmoo.cliente.escola.gradecurricular.service.IMateriaService;

@RestController
@RequestMapping("/materia")
public class MateriaController {

	/*
	 * RETIRADO O MATERIAREPOSITORY
	 */

	@Autowired
	private IMateriaService materiaService;

	@GetMapping
	public ResponseEntity<List<MateriaEntity>> listarMaterias() {
		return ResponseEntity.status(HttpStatus.OK).body(this.materiaService.listar());
	}

	@GetMapping("/{id}")
	public ResponseEntity<MateriaEntity> consultaMateria(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK).body(this.materiaService.consultar(id));
	}

	@PostMapping
	public ResponseEntity<Boolean> cadastrarMateria(@RequestBody MateriaEntity materia) {
		return ResponseEntity.status(HttpStatus.OK).body(this.materiaService.cadastrar(materia));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Boolean> excluirMateria(@PathVariable Long id) {

		return ResponseEntity.status(HttpStatus.OK).body(this.materiaService.excluir(id));

	}

	@PutMapping
	public ResponseEntity<Boolean> atualizarMateria(@RequestBody MateriaEntity materia) {

		return ResponseEntity.status(HttpStatus.OK).body(this.materiaService.atualizar(materia));

	}

}

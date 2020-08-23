package com.rasmoo.cliente.escola.gradecurricular.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.rasmoo.cliente.escola.gradecurricular.constant.MensagensConstant;
import com.rasmoo.cliente.escola.gradecurricular.entity.CursoEntity;
import com.rasmoo.cliente.escola.gradecurricular.entity.MateriaEntity;
import com.rasmoo.cliente.escola.gradecurricular.exception.CursoException;
import com.rasmoo.cliente.escola.gradecurricular.model.CursoModel;
import com.rasmoo.cliente.escola.gradecurricular.repository.ICursoRepository;
import com.rasmoo.cliente.escola.gradecurricular.repository.IMateriaRepository;

@CacheConfig(cacheNames = "curso")
@Service
public class CursoService implements ICursoService {

	private ICursoRepository cursoRepository;

	private IMateriaRepository materiaRepository;

	@Autowired
	public CursoService(ICursoRepository cursoRepository, IMateriaRepository materiaRepository) {
		this.cursoRepository = cursoRepository;
		this.materiaRepository = materiaRepository;
	}

	@Override
	public Boolean cadastrar(CursoModel cursoModel) {
		try {
			/*
			 * O id não pode ser informado no cadastro
			 */

			if (cursoModel.getId() != null) {
				throw new CursoException(MensagensConstant.ERRO_ID_INFORMADO.getValor(), HttpStatus.BAD_REQUEST);
			}

			/*
			 * Não permite fazer cadastro de cursos com mesmos códigos.
			 */
			if (this.cursoRepository.findCursoByCodigo(cursoModel.getCodCurso()) != null) {
				throw new CursoException(MensagensConstant.ERRO_CURSO_CADASTRADO_ANTERIORMENTE.getValor(),
						HttpStatus.BAD_REQUEST);
			}
			return this.cadastrarOuAtualizar(cursoModel);

		} catch (CursoException c) {
			throw c;
		} catch (Exception e) {
			throw new CursoException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Boolean atualizar(CursoModel cursoModel) {
		try {
			this.consultarPorCodigo(cursoModel.getCodCurso());
			return this.cadastrarOuAtualizar(cursoModel);
		} catch (CursoException c) {
			throw c;
		} catch (Exception e) {
			throw new CursoException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CachePut(key = "#codCurso")
	@Override
	public CursoEntity consultarPorCodigo(String codCurso) {

		try {
			CursoEntity curso = this.cursoRepository.findCursoByCodigo(codCurso);

			if (curso == null) {
				throw new CursoException(MensagensConstant.ERRO_CURSO_NAO_ENCONTRADO.getValor(), HttpStatus.NOT_FOUND);
			}

			return curso;

		} catch (CursoException c) {
			throw c;
		} catch (Exception e) {
			throw new CursoException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CachePut(unless = "#result.size()<3")
	@Override
	public List<CursoEntity> listar() {
		try {
			return this.cursoRepository.findAll();
		} catch (Exception e) {
			throw new CursoException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Boolean excluir(Long cursoId) {
		try {
			if (this.cursoRepository.findById(cursoId).isPresent()) {
				this.cursoRepository.deleteById(cursoId);
				return Boolean.TRUE;
			}
			throw new CursoException(MensagensConstant.ERRO_CURSO_NAO_ENCONTRADO.getValor(), HttpStatus.NOT_FOUND);
		} catch (CursoException c) {
			throw c;
		} catch (Exception e) {
			throw new CursoException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*
	 * O cadastrar e atualizar tem comportamentos semelhantes então centralizamos
	 * esss comportamento.
	 */

	private Boolean cadastrarOuAtualizar(CursoModel cursoModel) {
		List<MateriaEntity> listMateriaEntity = new ArrayList<>();

		if (cursoModel.getMaterias() != null && !cursoModel.getMaterias().isEmpty()) {

			cursoModel.getMaterias().forEach(materia -> {
				if (this.materiaRepository.findById(materia).isPresent())
					listMateriaEntity.add(this.materiaRepository.findById(materia).get());
			});
		}

		CursoEntity cursoEntity = new CursoEntity();
		if (cursoModel.getId() != null) {
			cursoEntity.setId(cursoModel.getId());
		}
		cursoEntity.setCodigo(cursoModel.getCodCurso());
		cursoEntity.setNome(cursoModel.getNome());
		cursoEntity.setMaterias(listMateriaEntity);

		this.cursoRepository.save(cursoEntity);

		return Boolean.TRUE;
	}

}

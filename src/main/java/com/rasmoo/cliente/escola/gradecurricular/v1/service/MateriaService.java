package com.rasmoo.cliente.escola.gradecurricular.v1.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.rasmoo.cliente.escola.gradecurricular.entity.MateriaEntity;
import com.rasmoo.cliente.escola.gradecurricular.repository.IMateriaRepository;
import com.rasmoo.cliente.escola.gradecurricular.v1.constant.MensagensConstant;
import com.rasmoo.cliente.escola.gradecurricular.v1.controller.MateriaController;
import com.rasmoo.cliente.escola.gradecurricular.v1.dto.MateriaDto;
import com.rasmoo.cliente.escola.gradecurricular.v1.exception.MateriaException;

@CacheConfig(cacheNames = "materia")
@Service
public class MateriaService implements IMateriaService {

	private IMateriaRepository materiaRepository;
	private ModelMapper mapper;

	@Autowired
	public MateriaService(IMateriaRepository materiaRepository) {
		this.mapper = new ModelMapper();
		this.materiaRepository = materiaRepository;
	}

	@Override
	public Boolean atualizar(MateriaDto materia) {

		try {
			this.consultar(materia.getId());
			return this.cadastrarOuAtualizar(materia);
		} catch (MateriaException m) {
			throw m;
		} catch (Exception e) {
			throw new MateriaException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Boolean excluir(Long id) {
		try {
			this.consultar(id);
			this.materiaRepository.deleteById(id);
			return Boolean.TRUE;
		} catch (MateriaException m) {
			throw m;
		} catch (Exception e) {
			throw new MateriaException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CachePut(key = "#id")
	@Override
	public MateriaDto consultar(Long id) {
		try {
			Optional<MateriaEntity> materiaOptional = this.materiaRepository.findById(id);
			if (materiaOptional.isPresent()) {
				return this.mapper.map(materiaOptional.get(), MateriaDto.class);
			}
			throw new MateriaException(MensagensConstant.ERRO_MATERIA_NAO_ENCONTRADA.getValor(), HttpStatus.NOT_FOUND);
		} catch (MateriaException m) {
			throw m;
		} catch (Exception e) {
			throw new MateriaException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CachePut(unless = "#result.size()<3")
	@Override
	public List<MateriaDto> listar() {
		try {
			List<MateriaDto> materiaDto = this.mapper.map(this.materiaRepository.findAll(),
					new TypeToken<List<MateriaDto>>() {
					}.getType());

			materiaDto.forEach(materia -> materia.add(WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class).consultarMateria(materia.getId()))
					.withSelfRel()));

			return materiaDto;

		} catch (Exception e) {
			throw new MateriaException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Boolean cadastrar(MateriaDto materia) {
		try {
			if (materia.getId() != null) {
				throw new MateriaException(MensagensConstant.ERRO_ID_INFORMADO.getValor(), HttpStatus.BAD_REQUEST);
			}

			if (this.materiaRepository.findByCodigo(materia.getCodigo()) != null) {
				throw new MateriaException(MensagensConstant.ERRO_MATERIA_CADASTRADA_ANTERIORMENTE.getValor(),
						HttpStatus.BAD_REQUEST);
			}
			return this.cadastrarOuAtualizar(materia);
		} catch (MateriaException m) {
			throw m;
		} catch (Exception e) {
			throw new MateriaException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public List<MateriaDto> listarPorHorarioMinimo(int horaMinima) {
		try {
			return this.mapper.map(this.materiaRepository.findByHoraMinima(horaMinima),
					new TypeToken<List<MateriaDto>>() {
					}.getType());
		} catch (Exception e) {
			throw new MateriaException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public List<MateriaDto> listarPorFrequencia(int frequencia) {
		try {
			return this.mapper.map(this.materiaRepository.findByFrequencia(frequencia),
					new TypeToken<List<MateriaDto>>() {
					}.getType());
		} catch (Exception e) {
			throw new MateriaException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private Boolean cadastrarOuAtualizar(MateriaDto materia) {
		MateriaEntity materiaEnt = this.mapper.map(materia, MateriaEntity.class);
		this.materiaRepository.save(materiaEnt);
		return Boolean.TRUE;
	}

}

package com.rasmoo.cliente.escola.gradecurricular.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rasmoo.cliente.escola.gradecurricular.entity.CursoEntity;

@Repository
public interface ICursoRepository extends JpaRepository<CursoEntity, Long>{

}

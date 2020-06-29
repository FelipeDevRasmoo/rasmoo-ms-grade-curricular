package com.rasmoo.cliente.escola.gradecurricular.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class MateriaException extends RuntimeException{

	private static final long serialVersionUID = 8042402636193525393L;
	
	private final HttpStatus httpStatus;
	
	public MateriaException(final String mensagem, final HttpStatus httpStatus) {
		super(mensagem);
		this.httpStatus = httpStatus;
	}
	
}

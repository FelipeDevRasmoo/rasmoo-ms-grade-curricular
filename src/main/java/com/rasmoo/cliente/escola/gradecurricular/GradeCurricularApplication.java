package com.rasmoo.cliente.escola.gradecurricular;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class GradeCurricularApplication extends SpringBootServletInitializer {
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(GradeCurricularApplication.class);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(GradeCurricularApplication.class, args);
	}

}

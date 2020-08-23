package com.rasmoo.cliente.escola.gradecurricular.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.hateoas.mediatype.collectionjson.CollectionJsonLinkDiscoverer;
import org.springframework.plugin.core.SimplePluginRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport {

	public static final String CURSO = "Curso";
	public static final String MATERIA = "Matéria";

	@Bean
	public Docket gradeCurricularApi() {
		return new Docket(DocumentationType.SWAGGER_2).useDefaultResponseMessages(false).select()
				.apis(RequestHandlerSelectors.basePackage("com.rasmoo.cliente.escola.gradecurricular")).build()
				.apiInfo(this.metaData()).tags(new Tag(CURSO, "Operações referentes a manipulação da entidade Curso."))
				.tags(new Tag(MATERIA, "Operações referentes a manipulação da entidade Materia."));
	}

	@Bean
	public LinkDiscoverers discoverers() {
		List<LinkDiscoverer> listPlugins = new ArrayList<>();
		listPlugins.add(new CollectionJsonLinkDiscoverer());
		return new LinkDiscoverers(SimplePluginRegistry.create(listPlugins));
	}

	private ApiInfo metaData() {
		return new ApiInfoBuilder().title("GRADE CURRICULAR")
				.description("Api responsável pela manutenção da grande curricular da RASMOO SCHOOL").version("1.0.0")
				.license("").build();
	}

	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:META-INF/resources/webjars/");
	}
}

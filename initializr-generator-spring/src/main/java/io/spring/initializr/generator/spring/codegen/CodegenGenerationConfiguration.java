package io.spring.initializr.generator.spring.codegen;

import org.springframework.context.annotation.Bean;

import io.spring.initializr.generator.condition.ConditionalOnSwaggerFile;
import io.spring.initializr.generator.project.CustomProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;

/**
 * Configuration for swagger file related contributions to a generated project.
 * 
 * @author Sayeed AP
 *
 */
@ProjectGenerationConfiguration
@ConditionalOnSwaggerFile("hasSwaggerFile")
public class CodegenGenerationConfiguration {

	@Bean
	public CodegenContributor codegenContributor(CustomProjectDescription description) {
		return new CodegenContributor(description);
	}

}

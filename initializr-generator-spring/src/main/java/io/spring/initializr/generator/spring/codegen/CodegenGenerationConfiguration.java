package io.spring.initializr.generator.spring.codegen;

import org.springframework.context.annotation.Bean;

import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;

/**
 * Configuration for swagger file related contributions to a generated project.
 * 
 * @author Sayeed
 *
 */
@ProjectGenerationConfiguration
public class CodegenGenerationConfiguration {

	@Bean
	public CodegenContributor codegenContributor(ProjectDescription description) {
		return new CodegenContributor(description);
	}

}

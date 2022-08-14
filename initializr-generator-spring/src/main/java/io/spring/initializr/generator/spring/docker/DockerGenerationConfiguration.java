package io.spring.initializr.generator.spring.docker;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuildSystem;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnBuildSystem;
import io.spring.initializr.generator.project.CustomProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.spring.scm.git.GitIgnore;
import io.spring.initializr.generator.spring.scm.git.GitIgnoreCustomizer;

@ProjectGenerationConfiguration
public class DockerGenerationConfiguration {
	
	@Bean
	public DockerContributor dockerContributor(DockerFileDeclaration description) {
		return new DockerContributor(description);
	}
	
	@Bean
	public DockerFileDeclaration dockerFileDeclaration(ObjectProvider<DockerCustomizer<DockerFileDeclaration>> gitIgnoreCustomizers) {
		DockerFileDeclaration gitIgnore = new DockerFileDeclaration();
		gitIgnoreCustomizers.orderedStream().forEach((customizer) -> customizer.customize(gitIgnore));
		return gitIgnore;
	}

	@Bean
	@ConditionalOnBuildSystem(MavenBuildSystem.ID)
	public DockerCustomizer<DockerFileDeclaration> mavenDockerCustomizer() {

		System.out.println("MavenBuildSystem.ID>>>>>>.");
		return (dockerTypeDeclaration) -> {
			dockerTypeDeclaration.setDockerBaseImage(new DockerBaseImage("Openjdf","Alfa"));
			
			
		};

	}

	@Bean
	@ConditionalOnBuildSystem(MavenBuildSystem.ID)
	public DockerCustomizer<DockerFileDeclaration> gradleDockerCustomizer() {
		System.out.println("GradleBuildSystem.ID");
		return null;

	};

}

package io.spring.initializr.generator.spring.docker;

import java.util.Arrays;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;

import io.spring.initializr.generator.condition.ConditionalOnPackaging;
import io.spring.initializr.generator.packaging.jar.JarPackaging;
import io.spring.initializr.generator.packaging.war.WarPackaging;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.spring.docker.Command.CommandType;

/**
 * Configuration for docker-related contributions to a generated project.
 * 
 * @author Sayeed
 *
 */
@ProjectGenerationConfiguration
public class DockerGenerationConfiguration {

	private final ProjectDescription description;

	public DockerGenerationConfiguration(ProjectDescription description) {
		super();
		this.description = description;
	}

	@Bean
	public DockerContributor dockerContributor(DockerFile dockerFile) {
		return new DockerContributor(dockerFile);
	}

	@Bean
	public DockerFile dockerFileDeclaration(ObjectProvider<DockerCustomizer<DockerFile>> dockerCustomizers) {
		DockerFile dockerFile = new DockerFile();
		dockerCustomizers.orderedStream().forEach((customizer) -> customizer.customize(dockerFile));
		return dockerFile;
	}

	@Bean
	@ConditionalOnPackaging(JarPackaging.ID)
	public DockerCustomizer<DockerFile> mavenDockerCustomizer() {
		return (dockerFile) -> {
			dockerFile.setImage(openjdkTagName(description.getLanguage().jvmVersion()));
			dockerFile.setCopy(new Copy("target/*.jar", "/" + description.getApplicationName() + ".jar"));
			dockerFile.setCommand(new Command(CommandType.ENTRYPOINT, "java",
					Arrays.asList("-java", "/" + description.getApplicationName() + ".jar")));
		};

	}

	@Bean
	@ConditionalOnPackaging(WarPackaging.ID)
	public DockerCustomizer<DockerFile> gradleDockerCustomizer() {
		return (dockerFile) -> {
			dockerFile.setImage(new Image("tomcat", "latest"));
			dockerFile.setCopy(new Copy("target/*.war", "/usr/local/tomcat/webapps/"));
			dockerFile.setCommand(new Command(CommandType.RUN, "catalina.sh", Arrays.asList("run")));
			dockerFile.setPort(8080);
		};

	};

	/**
	 * The {@link openjdkTagName} find appropriate docker image for java version
	 * 
	 * @param jvmVersion
	 * @return Docker image
	 */
	private Image openjdkTagName(String jvmVersion) {
		String tagName = "latest";
		switch (jvmVersion) {
		case "8":
			tagName = "8-jdk-alpine";
			break;
		case "11":
			tagName = "7u211-jdk-alpine";
			break;
		case "17":
			tagName = "17-jdk-alpine";
			break;
		case "18":
			tagName = "18-jdk-alpine";
			break;
		}
		return new Image("openjdk", tagName);
	}

}

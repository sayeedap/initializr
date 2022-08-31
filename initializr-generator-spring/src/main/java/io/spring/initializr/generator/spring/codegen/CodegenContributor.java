package io.spring.initializr.generator.spring.codegen;

import java.io.IOException;
import java.nio.file.Path;

import io.spring.initializr.generator.language.SourceStructure;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.contributor.ProjectContributor;

/**
 * {@link ProjectContributor} for the project's structure specified in {@code Swagger.yaml} file.
 * 
 * @author Sayeed
 *
 */
public class CodegenContributor implements ProjectContributor {

	private final ProjectDescription description;

	public CodegenContributor(ProjectDescription description) {
		this.description = description;
	}

	@Override
	public void contribute(Path projectRoot) throws IOException {
		// TODO
		// Application name = this.description.getApplicationName(); eg:
		// ProductApplication
		// Artifacte Id = this.description.getArtifactId(); eg: product
		// group id = this.description.getGroupId(); eg:com.litmus
		// language = this.description.getLanguage().id(); eg:java
		// language version = this.description.getLanguage().jvmVersion(); eg:17
		// package name = this.description.getPackageName(); eg:com.litmus.product
		// spring boot version = this.description.getPlatformVersion() eg:2.7.2

		SourceStructure sourceStructure = this.description.getBuildSystem().getMainSource(projectRoot,
				this.description.getLanguage());

		// resource directory sourceStructure.getResourcesDirectory();
		// C:\Users\sayeed\AppData\Local\Temp\project-13060793765327664540\product\src\main\resources
		// source directory sourceStructure.getSourcesDirectory();
		// //C:\Users\sayeed\AppData\Local\Temp\project-13060793765327664540\product\src\main\java

		writeSwaggerFile(sourceStructure);
	}

	private void writeSwaggerFile(SourceStructure sourceStructure) {
		// TODO Code for writing swagger file

		System.out.println("CodegenContributor Executed");

	}

}

package io.spring.initializr.generator.spring.codegen;

import java.io.IOException;
import java.nio.file.Path;

import io.spring.initializr.generator.language.SourceStructure;
import io.spring.initializr.generator.project.CustomProjectDescription;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.contributor.ProjectContributor;

/**
 * {@link ProjectContributor} for the project's structure specified in
 * {@code Swagger.yaml} file.
 * 
 * @author Sayeed
 *
 */
public class CodegenContributor implements ProjectContributor {

	private final CustomProjectDescription description;
	CliHelper cliHelper = new CliHelper();

	public CodegenContributor(CustomProjectDescription description) {
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

		writeSwaggerFile(projectRoot);
	}

	private void writeSwaggerFile(Path projectRoot) {
		// TODO Code for writing swagger file

		//ystem.out.println("swagger file" + this.description.getSwaggerFile());

		String args = generateCodeGenerationArgs(projectRoot);
		Object commandObject = cliHelper.setCommandObject(args);
		if (commandObject instanceof Runnable) {
			new Thread(((Runnable) commandObject)).start();
		}
	}

	private String generateCodeGenerationArgs(Path projectRoot) {
		StringBuilder codeGenArgs = new StringBuilder();
		String args = null;
		codeGenArgs.append("generate");
		if (cliHelper.isValidString(description.getGroupId()))
			codeGenArgs.append(" --group-id ").append(description.getGroupId());
		if (cliHelper.isValidString(description.getArtifactId()))
			codeGenArgs.append(" --artifact-id ").append(description.getArtifactId());

		String basePackage = description.getGroupId() + "." + description.getArtifactId();
		codeGenArgs.append(" --api-package ").append(basePackage + ".api");
		codeGenArgs.append(" --model-package ").append(basePackage + ".model");
		codeGenArgs.append(" --invoker-package ").append(basePackage);
		codeGenArgs.append(" --config-package ").append(basePackage + ".configuration");
		codeGenArgs.append(" --controller-package ").append(basePackage + ".controller");
		codeGenArgs.append(" --controllerimpl-package ").append(basePackage + ".controller.impl");
		codeGenArgs.append(" --exception-package ").append(basePackage + ".exception");
		codeGenArgs.append(" --library spring-cloud");
		setFramework(codeGenArgs);
		codeGenArgs.append(" -o ").append(projectRoot);

		// setInputFile(description);
		codeGenArgs.append(" -i ").append("C:\\Users\\stefy\\Desktop\\SwaggerCodeGenJar_11-08-2022\\APi1.yaml");
		System.out.println("stringBuilder:  " + codeGenArgs);

		args = codeGenArgs.toString();
		return args;
	}

	private void setFramework(StringBuilder codeGenArgs) {
		String framework = null;
		switch (this.description.getLanguage().toString()) {

		case "jaxrs-jersey":
			framework = "jaxrs-jersey";
			break;
		case "micronaut":
			framework = "micronaut";
			break;
		case "kotlin-server":
			framework = "kotlin-server";
			break;
		case "kotlin-client":
			framework = "kotlin-client";
			break;
		default:
			framework = "spring";
		}
		codeGenArgs.append(" -l ").append(framework);
	}

//		private void setInputFile(ProjectDescription description) {
//			//createDirIfNotExist();
//			byte[] bytes = new byte[0];
//			try {
//				bytes = swaggerRequest.getFile().getBytes();
//				Path tempDirWithPrefix = Files.createTempDirectory(prefix);
//				Files.write(Paths.get(tempDirWithPrefix + swaggerRequest.getFile().getOriginalFilename()), bytes);
//				swaggerRequest.setInputFile(Paths.get(tempDirWithPrefix + swaggerRequest.getFile().getOriginalFilename()).toString());
//			} catch (IOException ex) {
//				LOGGER.error("Error in setting inputFile: %s", ex.getMessage());
//			}
//		}

}

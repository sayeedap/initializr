/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.initializr.generator.spring.codegen;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import io.spring.initializr.generator.language.SourceStructure;
import io.spring.initializr.generator.project.CustomProjectDescription;
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

	public CodegenContributor(CustomProjectDescription description) {
		this.description = description;
	}

	@Override
	public void contribute(Path projectRoot) throws IOException {
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
		writeSwaggerFile(projectRoot);
	}

	private void writeSwaggerFile(Path projectRoot) {
		System.out.println("swagger file" + this.description.getSwaggerFile());
		String args = generateCodeGenerationArgs(projectRoot);
		CliHelper.setCommandObject(args);
		System.out.println("swagger code generation completed...");
		// if (commandObject instanceof Runnable) {
		// new Thread(((Runnable) commandObject)).start();
		// }
	}

	private String generateCodeGenerationArgs(Path projectRoot) {
		StringBuilder codeGenArgs = new StringBuilder();
		String args = null;
		codeGenArgs.append("generate");
		if (CliHelper.isValidString(description.getGroupId()))
			codeGenArgs.append(" --group-id ").append(description.getGroupId());
		if (CliHelper.isValidString(description.getArtifactId()))
			codeGenArgs.append(" --artifact-id ").append(description.getArtifactId());

		String basePackage = description.getGroupId() + "." + description.getArtifactId();
		codeGenArgs.append(" --api-package ").append(basePackage + ".api");
		codeGenArgs.append(" --model-package ").append(basePackage + ".model");
		codeGenArgs.append(" --invoker-package ").append(basePackage);
		codeGenArgs.append(" --config-package ").append(basePackage + ".configuration");
		codeGenArgs.append(" --controller-package ").append(basePackage + ".controller");
		codeGenArgs.append(" --controllerimpl-package ").append(basePackage + ".controller.impl");
		codeGenArgs.append(" --exception-package ").append(basePackage + ".exception");
		// codeGenArgs.append(" --library spring-cloud");
		setFramework(codeGenArgs);
		codeGenArgs.append(" -o ").append(projectRoot);

		setInputFile(codeGenArgs);
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

	private void setInputFile(StringBuilder codeGenArgs) {
		String prefix = "temp";
		byte[] bytes = new byte[0];
		try {
			bytes = this.description.getSwaggerFile().getBytes();
			Path tempDirWithPrefix = Files.createTempDirectory(prefix);
			Files.write(Paths.get(tempDirWithPrefix + this.description.getSwaggerFile().getOriginalFilename()), bytes);
			codeGenArgs.append(" -i ").append(
					Paths.get(tempDirWithPrefix + this.description.getSwaggerFile().getOriginalFilename()).toString());
		}
		catch (IOException ex) {
			throw new InvalidArgException("Error in setting inputFile: " + ex.getMessage());
		}
	}

}
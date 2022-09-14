package io.spring.initializr.generator.spring.code.java.components;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

import io.spring.initializr.generator.io.template.MustacheTemplateRenderer;
import io.spring.initializr.generator.io.text.MustacheSection;
import io.spring.initializr.generator.language.SourceStructure;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.contributor.ProjectContributor;

/**
 * @author Sayeed AP
 *
 */
public class ExceptionCodeContributor implements ProjectContributor {

	private final String packageName;

	private final ProjectDescription description;

	private final MustacheTemplateRenderer templateRenderer = new MustacheTemplateRenderer(
			"classpath:/templates/java/exception");

	public ExceptionCodeContributor(String packageName, ProjectDescription description) {
		this.packageName = packageName;
		this.description = description;
	}

	@Override
	public void contribute(Path projectRoot) throws IOException {
		SourceStructure sourceStructure = this.description.getBuildSystem().getMainSource(projectRoot,
				this.description.getLanguage());
		writeCustomException(sourceStructure);
		writeErrorResponsePOJO(sourceStructure);
		writeExceptionHandler(sourceStructure);
	}

	private void writeExceptionHandler(SourceStructure sourceStructure) throws IOException {
		Path file = sourceStructure.createSourceFile(this.packageName, "CustomException");
		MustacheSection mustacheSection = new MustacheSection(templateRenderer, "/CustomException",
				Collections.singletonMap("packageName", this.packageName));
		try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(file))) {
			mustacheSection.write(writer);
		}
	}

	private void writeErrorResponsePOJO(SourceStructure sourceStructure) throws IOException {
		Path file = sourceStructure.createSourceFile(this.packageName, "ErrorResponse");
		MustacheSection mustacheSection = new MustacheSection(templateRenderer, "/ErrorResponse",
				Collections.singletonMap("packageName", this.packageName));
		try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(file))) {
			mustacheSection.write(writer);
		}
	}

	private void writeCustomException(SourceStructure sourceStructure) throws IOException {
		Path file = sourceStructure.createSourceFile(this.packageName, "RestExceptionHandler");
		MustacheSection mustacheSection = new MustacheSection(templateRenderer, "/RestExceptionHandler",
				Collections.singletonMap("packageName", this.packageName));
		try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(file))) {
			mustacheSection.write(writer);
		}
	}

}

package io.spring.initializr.generator.spring.code.components;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.ObjectProvider;

import io.spring.initializr.generator.language.CompilationUnit;
import io.spring.initializr.generator.language.SourceCode;
import io.spring.initializr.generator.language.TypeDeclaration;
import io.spring.initializr.generator.spring.code.MainSourceCodeCustomizer;
import io.spring.initializr.generator.spring.util.LambdaSafe;

public class RepositoryCodeContributor implements
		MainSourceCodeCustomizer<TypeDeclaration, CompilationUnit<TypeDeclaration>, SourceCode<TypeDeclaration, CompilationUnit<TypeDeclaration>>> {

	private final String packageName;

	private final String initializerClassName;

	private final ObjectProvider<RepositoryCodeCustomizer<?>> repositoryCodeCustomizers;

	public RepositoryCodeContributor(String packageName, String initializerClassName,
			ObjectProvider<RepositoryCodeCustomizer<?>> repositoryCodeCustomizers) {
		this.packageName = packageName;
		this.initializerClassName = initializerClassName;
		this.repositoryCodeCustomizers = repositoryCodeCustomizers;
	}

	@Override
	public void customize(SourceCode<TypeDeclaration, CompilationUnit<TypeDeclaration>> sourceCode) {
		CompilationUnit<TypeDeclaration> compilationUnit = sourceCode
				.createCompilationUnit(this.packageName + ".repository", "Repository"); // File Name &&PACKAGENAME
		TypeDeclaration servletInitializer = compilationUnit.createTypeDeclaration("Repository"); // class
																													// anme
		servletInitializer.extend(this.initializerClassName);
		customizeServletInitializer(servletInitializer);
	}

	@SuppressWarnings("unchecked")
	private void customizeServletInitializer(TypeDeclaration servletInitializer) {
		List<RepositoryCodeCustomizer<?>> customizers = this.repositoryCodeCustomizers.orderedStream()
				.collect(Collectors.toList());
		LambdaSafe.callbacks(RepositoryCodeCustomizer.class, customizers, servletInitializer)
				.invoke((customizer) -> customizer.customize(servletInitializer));
	}

}

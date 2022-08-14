package io.spring.initializr.generator.spring.code.components;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.ObjectProvider;

import io.spring.initializr.generator.language.CompilationUnit;
import io.spring.initializr.generator.language.SourceCode;
import io.spring.initializr.generator.language.TypeDeclaration;
import io.spring.initializr.generator.spring.code.MainSourceCodeCustomizer;
import io.spring.initializr.generator.spring.util.LambdaSafe;

public class ExceptionCodeContributor implements
		MainSourceCodeCustomizer<TypeDeclaration, CompilationUnit<TypeDeclaration>, SourceCode<TypeDeclaration, CompilationUnit<TypeDeclaration>>> {

	private final String packageName;

	private final String initializerClassName;

	private final ObjectProvider<ExceptionCodeCustomizer<?>> exceptionCodeCustomizers;

	public ExceptionCodeContributor(String packageName, String initializerClassName,
			ObjectProvider<ExceptionCodeCustomizer<?>> exceptionCodeCustomizers) {
		this.packageName = packageName;
		this.initializerClassName = initializerClassName;
		this.exceptionCodeCustomizers = exceptionCodeCustomizers;
	}

	@Override
	public void customize(SourceCode<TypeDeclaration, CompilationUnit<TypeDeclaration>> sourceCode) {
		CompilationUnit<TypeDeclaration> compilationUnit = sourceCode
				.createCompilationUnit(this.packageName + ".exception", "CustomException"); // File Name &&PACKAGENAME
		TypeDeclaration servletInitializer = compilationUnit.createTypeDeclaration("CustomException"); // class																										// anme
		servletInitializer.extend(this.initializerClassName);
		customizeServletInitializer(servletInitializer);
	}

	@SuppressWarnings("unchecked")
	private void customizeServletInitializer(TypeDeclaration servletInitializer) {
		List<ExceptionCodeCustomizer<?>> customizers = this.exceptionCodeCustomizers.orderedStream()
				.collect(Collectors.toList());
		LambdaSafe.callbacks(ExceptionCodeCustomizer.class, customizers, servletInitializer)
				.invoke((customizer) -> customizer.customize(servletInitializer));
	}

}

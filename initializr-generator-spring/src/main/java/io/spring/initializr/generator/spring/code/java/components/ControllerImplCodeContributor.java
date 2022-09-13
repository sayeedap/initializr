package io.spring.initializr.generator.spring.code.java.components;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.ObjectProvider;

import io.spring.initializr.generator.language.Annotation;
import io.spring.initializr.generator.language.CompilationUnit;
import io.spring.initializr.generator.language.SourceCode;
import io.spring.initializr.generator.language.TypeDeclaration;
import io.spring.initializr.generator.spring.code.MainSourceCodeCustomizer;
import io.spring.initializr.generator.spring.util.LambdaSafe;

/**
 * @author Sayeed A
 *
 */
public class ControllerImplCodeContributor implements
		MainSourceCodeCustomizer<TypeDeclaration, CompilationUnit<TypeDeclaration>, SourceCode<TypeDeclaration, CompilationUnit<TypeDeclaration>>> {

	private final String fileName;

	private final String packageName;

	private final List<String> implementsClassName;

	private final ObjectProvider<ControllerImplCodeCustomizer<?>> controllerImplCodeCustomizers;

	public ControllerImplCodeContributor(String fileName, String packageName, List<String> implementsClassName,
			ObjectProvider<ControllerImplCodeCustomizer<?>> controllerImplCodeCustomizers) {
		this.fileName = fileName;
		this.packageName = packageName;
		this.implementsClassName = implementsClassName;
		this.controllerImplCodeCustomizers = controllerImplCodeCustomizers;
	}

	@Override
	public void customize(SourceCode<TypeDeclaration, CompilationUnit<TypeDeclaration>> sourceCode) {
		CompilationUnit<TypeDeclaration> compilationUnit = sourceCode
				.createCompilationUnit(this.packageName + ".controller.impl", fileName); // File
																							// Name
																							// &&PACKAGENAME
		TypeDeclaration servletInitializer = compilationUnit.createTypeDeclaration(fileName); // class
																								// //
																								// anme
		servletInitializer.implement(this.implementsClassName);
		servletInitializer.annotate(Annotation.name("org.springframework.web.bind.annotation.RestController"));
		servletInitializer.annotate(Annotation.name("org.springframework.web.bind.annotation.RequestMapping"));
		customizeServletInitializer(servletInitializer);
	}

	@SuppressWarnings("unchecked")
	private void customizeServletInitializer(TypeDeclaration servletInitializer) {
		List<ControllerImplCodeCustomizer<?>> customizers = this.controllerImplCodeCustomizers.orderedStream()
				.collect(Collectors.toList());
		LambdaSafe.callbacks(ControllerImplCodeCustomizer.class, customizers, servletInitializer)
				.invoke((customizer) -> customizer.customize(servletInitializer));
	}

}

package io.spring.initializr.generator.spring.code.components;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.ObjectProvider;

import io.spring.initializr.generator.language.CompilationUnit;
import io.spring.initializr.generator.language.SourceCode;
import io.spring.initializr.generator.language.TypeDeclaration;
import io.spring.initializr.generator.spring.code.MainSourceCodeCustomizer;
import io.spring.initializr.generator.spring.util.LambdaSafe;

public class ControllerImplCodeContributor implements
		MainSourceCodeCustomizer<TypeDeclaration, CompilationUnit<TypeDeclaration>, SourceCode<TypeDeclaration, CompilationUnit<TypeDeclaration>>> {

	private final String fileName;
	
	private final String packageName;

	private final String initializerClassName;

	private final ObjectProvider<ControllerImplCodeCustomizer<?>> controllerImplCodeCustomizers;

	public ControllerImplCodeContributor(String fileName,String packageName, String initializerClassName,
			ObjectProvider<ControllerImplCodeCustomizer<?>> controllerImplCodeCustomizers) {
		this.fileName=fileName;
		this.packageName = packageName;
		this.initializerClassName = initializerClassName;
		this.controllerImplCodeCustomizers = controllerImplCodeCustomizers;
	}

	@Override
	public void customize(SourceCode<TypeDeclaration, CompilationUnit<TypeDeclaration>> sourceCode) {
		CompilationUnit<TypeDeclaration> compilationUnit = sourceCode
				.createCompilationUnit(this.packageName + ".controller.impl", fileName); // File Name &&PACKAGENAME
		TypeDeclaration servletInitializer = compilationUnit.createTypeDeclaration(fileName); // class
																													// anme
		servletInitializer.extend(this.initializerClassName);
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

package io.spring.initializr.generator.spring.code.components;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.ObjectProvider;

import io.spring.initializr.generator.language.Annotation;
import io.spring.initializr.generator.language.CompilationUnit;
import io.spring.initializr.generator.language.SourceCode;
import io.spring.initializr.generator.language.TypeDeclaration;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.code.MainSourceCodeCustomizer;
import io.spring.initializr.generator.spring.util.LambdaSafe;

public class ControllerCodeContributor implements
		MainSourceCodeCustomizer<TypeDeclaration, CompilationUnit<TypeDeclaration>, SourceCode<TypeDeclaration, CompilationUnit<TypeDeclaration>>> {


	private final String fileName; 
	
	private final String packageName;

	private final String initializerClassName;

	private final ObjectProvider<ControllerCodeCustomizer<?>> controllerCodeCustomizers;

	public ControllerCodeContributor(String fileName,String packageName, String initializerClassName,
			ObjectProvider<ControllerCodeCustomizer<?>> controllerCodeCustomizers) {
		this.fileName=fileName;
		this.packageName = packageName;
		this.initializerClassName = initializerClassName;
		this.controllerCodeCustomizers = controllerCodeCustomizers;
	}

	@Override
	public void customize(SourceCode<TypeDeclaration, CompilationUnit<TypeDeclaration>> sourceCode) {
		CompilationUnit<TypeDeclaration> compilationUnit = sourceCode
				.createCompilationUnit(this.packageName + ".controller", fileName); // File Name &&PACKAGENAME
		TypeDeclaration servletInitializer = compilationUnit.createTypeDeclaration(fileName); // class
		servletInitializer.setClassType("interface");
		servletInitializer.annotate(Annotation.name("org.springframework.web.bind.annotation.RestController"));																											// anme
		servletInitializer.extend(this.initializerClassName);
		customizeServletInitializer(servletInitializer);
	}

	@SuppressWarnings("unchecked")
	private void customizeServletInitializer(TypeDeclaration servletInitializer) {
		List<ControllerCodeCustomizer<?>> customizers = this.controllerCodeCustomizers.orderedStream()
				.collect(Collectors.toList());
		LambdaSafe.callbacks(ControllerCodeCustomizer.class, customizers, servletInitializer)
				.invoke((customizer) -> customizer.customize(servletInitializer));
	}

}

package io.spring.initializr.generator.spring.code.java.components;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.ObjectProvider;

import io.spring.initializr.generator.language.CompilationUnit;
import io.spring.initializr.generator.language.SourceCode;
import io.spring.initializr.generator.language.TypeDeclaration;
import io.spring.initializr.generator.spring.code.MainSourceCodeCustomizer;
import io.spring.initializr.generator.spring.util.LambdaSafe;

/**
 * @author Sayeed
 *
 */
public class ServiceCodeContributor implements
		MainSourceCodeCustomizer<TypeDeclaration, CompilationUnit<TypeDeclaration>, SourceCode<TypeDeclaration, CompilationUnit<TypeDeclaration>>> {

	private final String fileName;
	
	private final String packageName;

	private final String initializerClassName;

	private final ObjectProvider<ServiceCodeCustomizer<?>> serviceCodeCustomizers;

	public ServiceCodeContributor(String fileName,String packageName, String initializerClassName,
			ObjectProvider<ServiceCodeCustomizer<?>> serviceCodeCustomizers) {
		this.fileName=fileName;
		this.packageName = packageName;
		this.initializerClassName = initializerClassName;
		this.serviceCodeCustomizers = serviceCodeCustomizers;
	}

	@Override
	public void customize(SourceCode<TypeDeclaration, CompilationUnit<TypeDeclaration>> sourceCode) {
		CompilationUnit<TypeDeclaration> compilationUnit = sourceCode
				.createCompilationUnit(this.packageName + ".service", fileName); // File Name &&PACKAGENAME
		TypeDeclaration servletInitializer = compilationUnit.createTypeDeclaration(fileName); // class
		servletInitializer.setClassType("interface");																											// anme
		servletInitializer.extend(this.initializerClassName);
		customizeServletInitializer(servletInitializer);
	}

	@SuppressWarnings("unchecked")
	private void customizeServletInitializer(TypeDeclaration servletInitializer) {
		List<ServiceCodeCustomizer<?>> customizers = this.serviceCodeCustomizers.orderedStream()
				.collect(Collectors.toList());
		LambdaSafe.callbacks(ServiceCodeCustomizer.class, customizers, servletInitializer)
				.invoke((customizer) -> customizer.customize(servletInitializer));
	}

}

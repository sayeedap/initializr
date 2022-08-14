/*
 * Copyright 2012-2021 the original author or authors.
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

package io.spring.initializr.generator.spring.code;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import io.spring.initializr.generator.condition.ConditionalOnComponents;
import io.spring.initializr.generator.condition.ConditionalOnPackaging;
import io.spring.initializr.generator.condition.ConditionalOnPlatformVersion;
import io.spring.initializr.generator.language.Annotation;
import io.spring.initializr.generator.language.TypeDeclaration;
import io.spring.initializr.generator.packaging.war.WarPackaging;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.spring.code.components.ControllerCodeContributor;
import io.spring.initializr.generator.spring.code.components.ControllerCodeCustomizer;
import io.spring.initializr.generator.spring.code.components.ControllerImplCodeContributor;
import io.spring.initializr.generator.spring.code.components.ControllerImplCodeCustomizer;
import io.spring.initializr.generator.spring.code.components.ExceptionCodeContributor;
import io.spring.initializr.generator.spring.code.components.ExceptionCodeCustomizer;
import io.spring.initializr.generator.spring.code.components.RepositoryCodeContributor;
import io.spring.initializr.generator.spring.code.components.RepositoryCodeCustomizer;
import io.spring.initializr.generator.spring.code.components.ServiceCodeContributor;
import io.spring.initializr.generator.spring.code.components.ServiceCodeCustomizer;
import io.spring.initializr.generator.spring.code.components.ServiceImplCodeContributor;
import io.spring.initializr.generator.spring.code.components.ServiceImplCodeCustomizer;

/**
 * Project generation configuration for projects written in any language.
 *
 * @author Andy Wilkinson
 */
@ProjectGenerationConfiguration
public class SourceCodeProjectGenerationConfiguration {

	@Bean
	public MainApplicationTypeCustomizer<TypeDeclaration> springBootApplicationAnnotator() {
		return (typeDeclaration) -> typeDeclaration
				.annotate(Annotation.name("org.springframework.boot.autoconfigure.SpringBootApplication"));
	}

	@Bean
	public TestApplicationTypeCustomizer<TypeDeclaration> junitJupiterSpringBootTestTypeCustomizer() {
		return (typeDeclaration) -> typeDeclaration
				.annotate(Annotation.name("org.springframework.boot.test.context.SpringBootTest"));
	}

	/**
	 * Language-agnostic source code contributions for projects using war packaging.
	 */
	@Configuration // class filente annotation okke evide kodukkanam
	@ConditionalOnPackaging(WarPackaging.ID)
	static class WarPackagingConfiguration {

		private final ProjectDescription description;

		WarPackagingConfiguration(ProjectDescription description) {
			this.description = description;
		}

		@Bean
		@ConditionalOnPlatformVersion("2.0.0.M1")
		ServletInitializerContributor boot20ServletInitializerContributor(
				ObjectProvider<ServletInitializerCustomizer<?>> servletInitializerCustomizers) {
			return new ServletInitializerContributor(this.description.getPackageName(),
					"org.springframework.boot.web.servlet.support.SpringBootServletInitializer",
					servletInitializerCustomizers);
		}

	}

	/**
	 * Language-agnostic source code contributions for projects using components.
	 */
	@Configuration // class filente annotation okke evide kodukkanam
	static class ComponentsConfiguration {

		private final ProjectDescription description;

		ComponentsConfiguration(ProjectDescription description) {
			this.description = description;
		}

		@Bean
		@ConditionalOnComponents("controller")
		ControllerCodeContributor controllerCodeContributor(
				ObjectProvider<ControllerCodeCustomizer<?>> controllerCodeCustomizer) {
			List<Annotation> annotations = new ArrayList<>();
			annotations.add(Annotation.name("org.springframework.web.bind.annotation.RestController"));
			return new ControllerCodeContributor(StringUtils.capitalize(this.description.getName())+"Controller"  ,this.description.getPackageName(), null, controllerCodeCustomizer);
		}

		@Bean
		@ConditionalOnComponents("controller")
		ControllerImplCodeContributor controllerImplCodeContributor(
				ObjectProvider<ControllerImplCodeCustomizer<?>> customCodeCustomizers) {
			List<Annotation> annotations = new ArrayList<>();
			annotations.add(Annotation.name("org.springframework.web.bind.annotation.RestController"));
			return new ControllerImplCodeContributor(StringUtils.capitalize(this.description.getName())+"ControllerImpl",this.description.getPackageName(), "com.litmus.demo.controller",
					customCodeCustomizers);
		}

		@Bean
		@ConditionalOnComponents("service")
		ServiceCodeContributor serviceCodeContributor(ObjectProvider<ServiceCodeCustomizer<?>> customCodeCustomizers) {
			List<Annotation> annotations = new ArrayList<>();
			return new ServiceCodeContributor(StringUtils.capitalize(this.description.getName())+"Service",this.description.getPackageName(), null, customCodeCustomizers);
		}

		@Bean
		@ConditionalOnComponents("service")
		ServiceImplCodeContributor serviceImplCodeContributor(
				ObjectProvider<ServiceImplCodeCustomizer<?>> customCodeCustomizers) {
			List<Annotation> annotations = new ArrayList<>();
			annotations.add(Annotation.name("org.springframework.stereotype.Service"));
			return new ServiceImplCodeContributor(StringUtils.capitalize(this.description.getName())+"ServiceImpl",this.description.getPackageName(), "com.litmus.demo.service",
					customCodeCustomizers);
		}

		@Bean
		RepositoryCodeContributor repositoryCodeContributor(
				ObjectProvider<RepositoryCodeCustomizer<?>> customCodeCustomizers) {
			List<Annotation> annotations = new ArrayList<>();
			return new RepositoryCodeContributor(this.description.getPackageName(), null, customCodeCustomizers);
		}

		@Bean
		@ConditionalOnComponents("exception")
		ExceptionCodeContributor exceptionCodeContributor(
				ObjectProvider<ExceptionCodeCustomizer<?>> customCodeCustomizers) {
			List<Annotation> annotations = new ArrayList<>();
			annotations.add(Annotation.name("org.springframework.web.bind.annotation.RestController"));
			return new ExceptionCodeContributor(this.description.getPackageName(), "java.lang.RuntimeException",
					customCodeCustomizers);
		}

	}	

}

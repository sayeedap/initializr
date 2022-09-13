/* Copyright 2012-2019 the original author or authors.
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

package io.spring.initializr.generator.spring.code.java.components;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.io.template.MustacheTemplateRenderer;
import io.spring.initializr.generator.io.text.MustacheSection;
import io.spring.initializr.generator.language.SourceStructure;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.contributor.ProjectContributor;
import io.spring.initializr.generator.spring.build.BuildMetadataResolver;
import io.spring.initializr.metadata.InitializrMetadata;

/**
 * @author Sayeed
 *
 */
public class ConfigurationCodesContributor implements ProjectContributor {

	private final ProjectDescription description;

	private final Build build;

	private final BuildMetadataResolver buildMetadataResolver;

	private final MustacheTemplateRenderer templateRenderer = new MustacheTemplateRenderer(
			"classpath:/templates/java/configuration");

	public ConfigurationCodesContributor(ProjectDescription description, Build build, InitializrMetadata metadata) {
		this.description = description;
		this.build = build;
		this.buildMetadataResolver = new BuildMetadataResolver(metadata);
	}

	@Override
	public void contribute(Path projectRoot) throws IOException {
		SourceStructure sourceStructure = this.description.getBuildSystem().getMainSource(projectRoot,
				this.description.getLanguage());

		if (this.buildMetadataResolver.hasFacet(this.build, "swagger")) {

			writeSwaggerConfiguration(sourceStructure);
		}
		else {
			System.out.println("Swagger noooooot found");
		}

	}

	private void writeSwaggerConfiguration(SourceStructure sourceStructure) throws IOException {
		Path file = sourceStructure.createSourceFile(this.description.getPackageName() + ".comfiguration",
				"SwaggerConfig");
		MustacheSection mustacheSection = new MustacheSection(templateRenderer, "/SwaggerConfig",
				Collections.singletonMap("packageName", this.description.getPackageName() + ".configuration"));
		try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(file))) {
			mustacheSection.write(writer);
		}
	}

}

package io.spring.initializr.generator.spring.code.java.components;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
public class OtherComponentsContributor implements ProjectContributor {

	private final ProjectDescription description;

	private final Build build;

	private final BuildMetadataResolver buildMetadataResolver;

	private final MustacheTemplateRenderer templateRenderer = new MustacheTemplateRenderer(
			"classpath:/templates/java/");

	public OtherComponentsContributor(ProjectDescription description, Build build, InitializrMetadata metadata) {
		this.description = description;
		this.build = build;
		this.buildMetadataResolver = new BuildMetadataResolver(metadata);
	}

	public Build getBuild() {
		return build;
	}

	public BuildMetadataResolver getBuildMetadataResolver() {
		return buildMetadataResolver;
	}

	public MustacheTemplateRenderer getTemplateRenderer() {
		return templateRenderer;
	}

	@Override
	public void contribute(Path projectRoot) throws IOException {
		SourceStructure sourceStructure = this.description.getBuildSystem().getMainSource(projectRoot,
				this.description.getLanguage());
		List<ProjectComponents> components = Arrays.asList(ProjectComponents.values());
		components.stream().filter(component -> component.defaultComponent).iterator().forEachRemaining(component -> {
			createComponent(sourceStructure, component);
		});

		// Create package named 'proxy' if it has dependency for feign client
		if (buildMetadataResolver.hasFacet(build, "OpenFeign")) {
			createComponent(sourceStructure, ProjectComponents.PROXY);
		}

	}

	private void createComponent(SourceStructure sourceStructure, ProjectComponents component) {
		Path file;
		try {
			file = sourceStructure.createSourceFile(description.getPackageName() + component.getPackageName(),
					"package-info");
			MustacheSection mustacheSection = new MustacheSection(templateRenderer, "package-info",
					component.convertToMustacheMap(description.getPackageName()));
			try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(file))) {
				mustacheSection.write(writer);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public enum ProjectComponents {

		CONFIGURATION(".configuration",
				"Provides the classes necessary for project structure or configuration assets based on requested dependencies.",
				true),

		CONTROLLER(".controller", "Provides interfaces for controllers", true),

		CONTROLLER_IMPL(".controller.impl", "Implementations of controllers", true),

		SERVICE(".service", "Provides interfaces for service", true),

		SERVICE_IMPL(".service.impl", "Implementations of services ", true),

		ENTITY(".entity", "Contains entity classess", true),

		MODEL(".model", "Provides model classes", true),

		EXCEPTION(".exception", "Classes for handing the exceptions", true),

		PROXY(".proxy", "Contains proxy classes", false),

		REPOSITORY(".repository", "Provides repository classes", true),

		UTILITY(".utility", "Miscellaneous utility classes.", true),;

		private final String packageName;

		private final String about;

		private final boolean defaultComponent;

		ProjectComponents(String packageName, String about, boolean defaultComponent) {
			this.packageName = packageName;
			this.about = about;
			this.defaultComponent = defaultComponent;
		}

		public String getPackageName() {
			return packageName;
		}

		public String getAbout() {
			return about;
		}

		public boolean isDefaultComponent() {
			return defaultComponent;
		}

		public Map<String, Object> convertToMustacheMap(String basePackage) {
			return Map.of("packageName", basePackage + this.packageName, "about", this.about);
		}

	}

}

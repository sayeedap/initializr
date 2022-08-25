package io.spring.initializr.generator.spring.code.java.components;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.spring.initializr.generator.io.template.MustacheTemplateRenderer;
import io.spring.initializr.generator.io.text.MustacheSection;
import io.spring.initializr.generator.language.SourceStructure;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.contributor.ProjectContributor;

/**
 * @author Sayeed
 *
 */
public class OtherComponentsContributor implements ProjectContributor {

	private final ProjectDescription description;

	private final MustacheTemplateRenderer templateRenderer = new MustacheTemplateRenderer(
			"classpath:/templates/java/");

	public OtherComponentsContributor(ProjectDescription description) {
		this.description = description;
	}

	@Override
	public void contribute(Path projectRoot) throws IOException {
		SourceStructure sourceStructure = this.description.getBuildSystem().getMainSource(projectRoot,
				this.description.getLanguage());

		List<ProjectComponents> components = Arrays.asList(ProjectComponents.values());

		components.iterator().forEachRemaining(component -> {

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

		});

//		Files.createDirectories(resolvePackage(projectRoot,
//				sourceStructure.getSourcesDirectory() + "." + this.description.getPackageName() + ".configuration"));
//		Files.createDirectories(resolvePackage(projectRoot,
//				sourceStructure.getSourcesDirectory() + "." + this.description.getPackageName() + ".repository"));
//		Files.createDirectories(resolvePackage(projectRoot,
//				sourceStructure.getSourcesDirectory() + "." + this.description.getPackageName() + ".utility"));
//		Files.createDirectories(resolvePackage(projectRoot,
//				sourceStructure.getSourcesDirectory() + "." + this.description.getPackageName() + ".proxy"));
//		Files.createDirectories(resolvePackage(projectRoot,
//				sourceStructure.getSourcesDirectory() + "." + this.description.getPackageName() + ".model"));
//		Files.createDirectories(resolvePackage(projectRoot,
//				sourceStructure.getSourcesDirectory() + "." + this.description.getPackageName() + ".entity"));

	}

//	private static Path resolvePackage(Path directory, String packageName) {
//		return directory.resolve(packageName.replace('.', '/'));
//	}

	public enum ProjectComponents {

		CONFIGURATION(".configuration",
				"Provides the classes necessary for project structure or configuration assets based on requested dependencies."),

		CONTROLLER(".controller", "Provides interfaces for controllers"),

		CONTROLLER_IMPL(".controller.impl", "Implementations of controllers"),

		SERVICE(".service", "Provides interfaces for service"),

		SERVICE_IMPL(".service.impl", "Implementations of services "),

		ENTITY(".entity", "Contains entity classess"),

		MODEL(".model", "Provides model classes"),

		EXCEPTION(".exception", "Classes for handing the exceptions"),

		PROXY(".proxy", "Contains proxy classes"),

		REPOSITORY(".repository", "Provides repository classes"),

		UTILITY(".utility", "Miscellaneous utility classes."),;

		private final String packageName;

		private final String about;

		ProjectComponents(String packageName, String about) {
			this.packageName = packageName;
			this.about = about;
		}

		public String getPackageName() {
			return packageName;
		}

		public String getAbout() {
			return about;
		}

		public Map<String, Object> convertToMustacheMap(String basePackage) {
			return Map.of("packageName", basePackage + this.packageName, "about", this.about);
		}

	}

}

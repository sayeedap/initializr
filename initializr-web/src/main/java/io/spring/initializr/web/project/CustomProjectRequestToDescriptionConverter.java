/*
 * Copyright 2012-2020 the original author or authors.
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

package io.spring.initializr.web.project;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import io.spring.initializr.generator.buildsystem.BuildSystem;
import io.spring.initializr.generator.language.Language;
import io.spring.initializr.generator.packaging.Packaging;
import io.spring.initializr.generator.project.CustomProjectDescription;
import io.spring.initializr.generator.project.MutableProjectDescription;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.metadata.DefaultMetadataElement;
import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.metadata.Type;
import io.spring.initializr.metadata.InitializrConfiguration.Platform;
import io.spring.initializr.metadata.support.MetadataBuildItemMapper;

/**
 * A default {@link ProjectRequestToDescriptionConverter} implementation that uses the
 * {@link InitializrMetadata metadata} to set default values for missing attributes if
 * necessary. Transparently transform the platform version if necessary using a
 * {@link ProjectRequestPlatformVersionTransformer}.
 *
 * @author Madhura Bhave
 * @author HaiTao Zhang
 * @author Stephane Nicoll
 * @author Sayeed
 */
public class CustomProjectRequestToDescriptionConverter
		implements ProjectRequestToDescriptionConverter<CustomProjectRequest> {

	private final ProjectRequestPlatformVersionTransformer platformVersionTransformer;

	public CustomProjectRequestToDescriptionConverter() {
		this((version, metadata) -> version);
	}

	public CustomProjectRequestToDescriptionConverter(
			ProjectRequestPlatformVersionTransformer platformVersionTransformer) {
		Assert.notNull(platformVersionTransformer, "PlatformVersionTransformer must not be null");
		this.platformVersionTransformer = platformVersionTransformer;
	}

	@Override
	public ProjectDescription convert(CustomProjectRequest request, InitializrMetadata metadata) {
		CustomProjectDescription description = new CustomProjectDescription();
		convert(request, description, metadata);
		return description;
	}

	/**
	 * Validate the specified {@link ProjectRequest request} and initialize the specified
	 * {@link ProjectDescription description}. Override any attribute of the description
	 * that are managed by this instance.
	 * @param request the request to validate
	 * @param description the description to initialize
	 * @param metadata the metadata instance to use to apply defaults if necessary
	 */
	public void convert(CustomProjectRequest request, CustomProjectDescription description,
			InitializrMetadata metadata) {
		validate(request, metadata);
		Version platformVersion = getPlatformVersion(request, metadata);
		List<Dependency> resolvedDependencies = getResolvedDependencies(request, platformVersion, metadata);
		validateDependencyRange(platformVersion, resolvedDependencies);

		description.setApplicationName(request.getApplicationName());
		description.setArtifactId(request.getArtifactId());
		description.setBaseDirectory(request.getBaseDir());
		description.setBuildSystem(getBuildSystem(request, metadata));
		description.setDescription(request.getDescription());
		description.setGroupId(request.getGroupId());
		description.setLanguage(Language.forId(request.getLanguage(), request.getJavaVersion()));
		description.setName(request.getName());
		description.setPackageName(request.getPackageName());
		description.setPackaging(Packaging.forId(request.getPackaging()));
		description.setPlatformVersion(platformVersion);
		description.setVersion(request.getVersion());
		resolvedDependencies.forEach((dependency) -> description.addDependency(dependency.getId(),
				MetadataBuildItemMapper.toDependency(dependency)));
		description.setDockerFlag(request.getDockerFlag());
		description.setProjectComponents(request.getProjectComponents());
		description.setSwaggerFile(request.getSwaggerFile());

	}

	private void validate(CustomProjectRequest request, InitializrMetadata metadata) {
		validatePlatformVersion(request, metadata);
		validateType(request.getType(), metadata);
		validateLanguage(request.getLanguage(), metadata);
		validatePackaging(request.getPackaging(), metadata);
		validateDependencies(request, metadata);
		validateSwaggerFile(request.getSwaggerFile(), metadata);
	}

	private void validateSwaggerFile(MultipartFile file, InitializrMetadata metadata) {

		List<String> SUPPORTED_MIME_TYPE = Arrays.asList("application/json", "text/yaml");
		long FIVE_MB_IN_BYTES = 5242880;
		if (file != null && !file.isEmpty()) {
			if (file.getSize() > FIVE_MB_IN_BYTES) {
				throw new InvalidProjectRequestException("Swagger File exceeded size");
			}

			if (!SUPPORTED_MIME_TYPE.contains(file.getContentType())) {
				throw new InvalidProjectRequestException("Swagger File invalid type");
			}
		}

	}

	private void validatePlatformVersion(ProjectRequest request, InitializrMetadata metadata) {
		Version platformVersion = Version.safeParse(request.getBootVersion());
		Platform platform = metadata.getConfiguration().getEnv().getPlatform();
		if (platformVersion != null && !platform.isCompatibleVersion(platformVersion)) {
			throw new InvalidProjectRequestException("Invalid Spring Boot version '" + platformVersion
					+ "', Spring Boot compatibility range is " + platform.determineCompatibilityRangeRequirement());
		}
	}

	private void validateType(String type, InitializrMetadata metadata) {
		if (type != null) {
			Type typeFromMetadata = metadata.getTypes().get(type);
			if (typeFromMetadata == null) {
				throw new InvalidProjectRequestException("Unknown type '" + type + "' check project metadata");
			}
			if (!typeFromMetadata.getTags().containsKey("build")) {
				throw new InvalidProjectRequestException(
						"Invalid type '" + type + "' (missing build tag) check project metadata");
			}
		}
	}

	private void validateLanguage(String language, InitializrMetadata metadata) {
		if (language != null) {
			DefaultMetadataElement languageFromMetadata = metadata.getLanguages().get(language);
			if (languageFromMetadata == null) {
				throw new InvalidProjectRequestException("Unknown language '" + language + "' check project metadata");
			}
		}
	}

	private void validatePackaging(String packaging, InitializrMetadata metadata) {
		if (packaging != null) {
			DefaultMetadataElement packagingFromMetadata = metadata.getPackagings().get(packaging);
			if (packagingFromMetadata == null) {
				throw new InvalidProjectRequestException(
						"Unknown packaging '" + packaging + "' check project metadata");
			}
		}
	}

	private void validateDependencies(ProjectRequest request, InitializrMetadata metadata) {
		List<String> dependencies = request.getDependencies();
		dependencies.forEach((dep) -> {
			Dependency dependency = metadata.getDependencies().get(dep);
			if (dependency == null) {
				throw new InvalidProjectRequestException("Unknown dependency '" + dep + "' check project metadata");
			}
		});
	}

	private void validateDependencyRange(Version platformVersion, List<Dependency> resolvedDependencies) {
		resolvedDependencies.forEach((dep) -> {
			if (!dep.match(platformVersion)) {
				throw new InvalidProjectRequestException(
						"Dependency '" + dep.getId() + "' is not compatible " + "with Spring Boot " + platformVersion);
			}
		});
	}

	private BuildSystem getBuildSystem(ProjectRequest request, InitializrMetadata metadata) {
		Type typeFromMetadata = metadata.getTypes().get(request.getType());
		return BuildSystem.forId(typeFromMetadata.getTags().get("build"));
	}

	private Version getPlatformVersion(ProjectRequest request, InitializrMetadata metadata) {
		String versionText = (request.getBootVersion() != null) ? request.getBootVersion()
				: metadata.getBootVersions().getDefault().getId();
		Version version = Version.parse(versionText);
		return this.platformVersionTransformer.transform(version, metadata);
	}

	private List<Dependency> getResolvedDependencies(ProjectRequest request, Version platformVersion,
			InitializrMetadata metadata) {
		List<String> depIds = request.getDependencies();
		return depIds.stream().map((it) -> {
			Dependency dependency = metadata.getDependencies().get(it);
			return dependency.resolve(platformVersion);
		}).collect(Collectors.toList());
	}

}
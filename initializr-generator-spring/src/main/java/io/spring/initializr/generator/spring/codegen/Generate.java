/*
 * Copyright 2012-2019 the original author or authors.
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

package io.spring.initializr.generator.spring.codegen;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import io.swagger.codegen.v3.ClientOptInput;
import io.swagger.codegen.v3.CodegenArgument;
import io.swagger.codegen.v3.CodegenConstants;
import io.swagger.codegen.v3.DefaultGenerator;
import io.swagger.codegen.v3.config.CodegenConfigurator;
import io.swagger.codegen.v3.config.CodegenConfiguratorUtils;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.parser.util.RemoteUrl;

/**
 * Generate is used for swagger code generation.
 *
 * @author stefy joshy
 */

// public class Generate implements Runnable {
public class Generate {

	CliHelper cliHelper;

	protected Boolean verbose;

	protected String lang;

	protected String output = "";

	protected String spec;

	protected String templateDir;

	protected String templateVersion;

	protected String templateEngine;

	protected String auth;

	protected List<String> systemProperties = new ArrayList<>();

	protected String configFile;

	protected Boolean skipOverwrite;

	protected String apiPackage;

	protected String modelPackage;

	protected String configPackage;

	protected String controllerPackage;

	protected String controllerImplPackage;

	protected String exceptionPackage;

	protected String modelNamePrefix;

	protected String modelNameSuffix;

	protected List<String> instantiationTypes = new ArrayList<>();

	protected List<String> typeMappings = new ArrayList<>();

	protected List<String> additionalProperties = new ArrayList<>();

	protected List<String> languageSpecificPrimitives = new ArrayList<>();

	protected List<String> importMappings = new ArrayList<>();

	protected String invokerPackage;

	protected String groupId;

	protected String artifactId;

	protected String artifactVersion;

	protected String library;

	protected String gitUserId;

	protected String gitRepoId;

	protected String gitRepoBaseURL;

	protected String releaseNote;

	protected String httpUserAgent;

	protected List<String> reservedWordsMappings = new ArrayList<>();

	protected String ignoreFileOverride;

	protected Boolean removeOperationIdPrefix;

	protected Boolean disableExamples;

	protected Boolean resolveFully;

	protected Boolean ignoreImportMappings;

	protected Boolean flattenInlineSchema;

	private String url;

	private List<CodegenArgument> codegenArguments;

	public void setVerbose(Boolean verbose) {
		this.verbose = verbose;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public void setTemplateDir(String templateDir) {
		this.templateDir = templateDir;
	}

	public void setTemplateVersion(String templateVersion) {
		this.templateVersion = templateVersion;
	}

	public void setTemplateEngine(String templateEngine) {
		this.templateEngine = templateEngine;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public void setSystemProperties(List<String> systemProperties) {
		this.systemProperties = systemProperties;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	public void setSkipOverwrite(Boolean skipOverwrite) {
		this.skipOverwrite = skipOverwrite;
	}

	public void setApiPackage(String apiPackage) {
		this.apiPackage = apiPackage;
	}

	public void setModelPackage(String modelPackage) {
		this.modelPackage = modelPackage;
	}

	public void setConfigPackage(String configPackage) {
		this.configPackage = configPackage;
	}

	public void setControllerPackage(String controllerPackage) {
		this.controllerPackage = controllerPackage;
	}

	public void setControllerImplPackage(String controllerImplPackage) {
		this.controllerImplPackage = controllerImplPackage;
	}

	public void setExceptionPackage(String exceptionPackage) {
		this.exceptionPackage = exceptionPackage;
	}

	public void setModelNamePrefix(String modelNamePrefix) {
		this.modelNamePrefix = modelNamePrefix;
	}

	public void setModelNameSuffix(String modelNameSuffix) {
		this.modelNameSuffix = modelNameSuffix;
	}

	public void setInstantiationTypes(List<String> instantiationTypes) {
		this.instantiationTypes = instantiationTypes;
	}

	public void setTypeMappings(List<String> typeMappings) {
		this.typeMappings = typeMappings;
	}

	public void setAdditionalProperties(List<String> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}

	public void setLanguageSpecificPrimitives(List<String> languageSpecificPrimitives) {
		this.languageSpecificPrimitives = languageSpecificPrimitives;
	}

	public void setImportMappings(List<String> importMappings) {
		this.importMappings = importMappings;
	}

	public void setInvokerPackage(String invokerPackage) {
		this.invokerPackage = invokerPackage;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public void setArtifactVersion(String artifactVersion) {
		this.artifactVersion = artifactVersion;
	}

	public void setLibrary(String library) {
		this.library = library;
	}

	public void setGitUserId(String gitUserId) {
		this.gitUserId = gitUserId;
	}

	public void setGitRepoId(String gitRepoId) {
		this.gitRepoId = gitRepoId;
	}

	public void setGitRepoBaseURL(String gitRepoBaseURL) {
		this.gitRepoBaseURL = gitRepoBaseURL;
	}

	public void setReleaseNote(String releaseNote) {
		this.releaseNote = releaseNote;
	}

	public void setHttpUserAgent(String httpUserAgent) {
		this.httpUserAgent = httpUserAgent;
	}

	public void setReservedWordsMappings(List<String> reservedWordsMappings) {
		this.reservedWordsMappings = reservedWordsMappings;
	}

	public void setIgnoreFileOverride(String ignoreFileOverride) {
		this.ignoreFileOverride = ignoreFileOverride;
	}

	public void setRemoveOperationIdPrefix(Boolean removeOperationIdPrefix) {
		this.removeOperationIdPrefix = removeOperationIdPrefix;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setCodegenArguments(List<CodegenArgument> codegenArguments) {
		this.codegenArguments = codegenArguments;
	}

	public void setDisableExamples(Boolean disableExamples) {
		this.disableExamples = disableExamples;
	}

	public void setResolveFully(Boolean resolveFully) {
		this.resolveFully = resolveFully;
	}

	public void setFlattenInlineSchema(Boolean flattenInlineSchema) {
		this.flattenInlineSchema = flattenInlineSchema;
	}

	public void setIgnoreImportMappings(Boolean ignoreImportMappings) {
		this.ignoreImportMappings = ignoreImportMappings;
	}

	// @Override
	// public void run() {
	public void generateSwaggerCode() {
		loadArguments();

		// attempt to read from config file
		CodegenConfigurator configurator = CodegenConfigurator.fromFile(this.configFile);

		// if a config file wasn't specified or we were unable to read it
		if (configurator == null) {
			// create a fresh configurator
			configurator = new CodegenConfigurator();
		}

		// now override with any specified parameters
		if (this.verbose != null) {
			configurator.setVerbose(this.verbose);
		}

		if (this.skipOverwrite != null) {
			configurator.setSkipOverwrite(this.skipOverwrite);
		}

		if (StringUtils.isNotEmpty(this.spec)) {
			configurator.setInputSpecURL(this.spec);
		}

		if (StringUtils.isNotEmpty(this.lang)) {
			configurator.setLang(this.lang);
		}

		if (StringUtils.isNotEmpty(this.output)) {
			configurator.setOutputDir(this.output);
		}

		if (StringUtils.isNotEmpty(this.auth)) {
			configurator.setAuth(this.auth);
		}

		if (StringUtils.isNotEmpty(this.templateDir)) {
			configurator.setTemplateDir(this.templateDir);
		}

		if (StringUtils.isNotEmpty(this.templateVersion)) {
			configurator.setTemplateVersion(this.templateVersion);
		}

		if (StringUtils.isNotEmpty(this.apiPackage)) {
			configurator.setApiPackage(this.apiPackage);
		}

		if (StringUtils.isNotEmpty(this.modelPackage)) {
			configurator.setModelPackage(this.modelPackage);
		}

		if (StringUtils.isNotEmpty(this.configPackage)) {
			configurator.setConfigPackage(this.configPackage);
		}

		if (StringUtils.isNotEmpty(this.controllerPackage)) {
			configurator.setControllerPackage(this.controllerPackage);
		}

		if (StringUtils.isNotEmpty(this.controllerImplPackage)) {
			configurator.setControllerImplPackage(this.controllerImplPackage);
		}

		if (StringUtils.isNotEmpty(this.exceptionPackage)) {
			configurator.setExceptionPackage(this.exceptionPackage);
		}

		if (StringUtils.isNotEmpty(this.modelNamePrefix)) {
			configurator.setModelNamePrefix(this.modelNamePrefix);
		}

		if (StringUtils.isNotEmpty(this.modelNameSuffix)) {
			configurator.setModelNameSuffix(this.modelNameSuffix);
		}

		if (StringUtils.isNotEmpty(this.invokerPackage)) {
			configurator.setInvokerPackage(this.invokerPackage);
		}

		if (StringUtils.isNotEmpty(this.groupId)) {
			configurator.setGroupId(this.groupId);
		}

		if (StringUtils.isNotEmpty(this.artifactId)) {
			configurator.setArtifactId(this.artifactId);
		}

		if (StringUtils.isNotEmpty(this.artifactVersion)) {
			configurator.setArtifactVersion(this.artifactVersion);
		}

		if (StringUtils.isNotEmpty(this.library)) {
			configurator.setLibrary(this.library);
		}

		if (StringUtils.isNotEmpty(this.gitUserId)) {
			configurator.setGitUserId(this.gitUserId);
		}

		if (StringUtils.isNotEmpty(this.gitRepoId)) {
			configurator.setGitRepoId(this.gitRepoId);
		}

		if (StringUtils.isNotEmpty(this.gitRepoBaseURL)) {
			configurator.setGitRepoBaseURL(this.gitRepoBaseURL);
		}

		if (StringUtils.isNotEmpty(this.releaseNote)) {
			configurator.setReleaseNote(this.releaseNote);
		}

		if (StringUtils.isNotEmpty(this.httpUserAgent)) {
			configurator.setHttpUserAgent(this.httpUserAgent);
		}

		if (StringUtils.isNotEmpty(this.ignoreFileOverride)) {
			configurator.setIgnoreFileOverride(this.ignoreFileOverride);
		}

		if (this.flattenInlineSchema != null) {
			configurator.setFlattenInlineSchema(this.flattenInlineSchema);
		}

		if (this.removeOperationIdPrefix != null) {
			configurator.setRemoveOperationIdPrefix(this.removeOperationIdPrefix);
		}

		if (this.codegenArguments != null && !this.codegenArguments.isEmpty()) {
			configurator.setCodegenArguments(this.codegenArguments);
		}

		if (this.disableExamples != null && this.disableExamples) {
			this.additionalProperties.add(
					String.format("%s=%s", CodegenConstants.DISABLE_EXAMPLES_OPTION, this.disableExamples.toString()));
		}

		if (this.ignoreImportMappings != null) {
			this.additionalProperties.add(String.format("%s=%s", CodegenConstants.IGNORE_IMPORT_MAPPING_OPTION,
					Boolean.parseBoolean(this.ignoreImportMappings.toString())));
		}

		if (this.resolveFully != null) {
			configurator.setResolveFully(this.resolveFully);
		}

		if (CodegenConstants.MUSTACHE_TEMPLATE_ENGINE.equalsIgnoreCase(this.templateEngine)) {
			this.additionalProperties.add(String.format("%s=%s", CodegenConstants.TEMPLATE_ENGINE,
					CodegenConstants.MUSTACHE_TEMPLATE_ENGINE));
		}
		else {
			this.additionalProperties.add(String.format("%s=%s", CodegenConstants.TEMPLATE_ENGINE,
					CodegenConstants.HANDLEBARS_TEMPLATE_ENGINE));
		}
		CodegenConfiguratorUtils codegenConfiguratorUtils = new CodegenConfiguratorUtils();

		codegenConfiguratorUtils.applySystemPropertiesKvpList(this.systemProperties, configurator);
		codegenConfiguratorUtils.applyInstantiationTypesKvpList(this.instantiationTypes, configurator);
		codegenConfiguratorUtils.applyImportMappingsKvpList(this.importMappings, configurator);
		codegenConfiguratorUtils.applyTypeMappingsKvpList(this.typeMappings, configurator);
		codegenConfiguratorUtils.applyAdditionalPropertiesKvpList(this.additionalProperties, configurator);
		codegenConfiguratorUtils.applyLanguageSpecificPrimitivesCsvList(this.languageSpecificPrimitives, configurator);
		codegenConfiguratorUtils.applyReservedWordsMappingsKvpList(this.reservedWordsMappings, configurator);
		final ClientOptInput clientOptInput = configurator.toClientOptInput();

		new DefaultGenerator().opts(clientOptInput).generate();
		deleteTempInputFile(configurator.getInputSpecURL());
	}

	private void deleteTempInputFile(String inputFile) {
		File file = new File(inputFile);
		try {
			FileUtils.delete(file);
		}
		catch (IOException ex) {
			throw new InvalidArgException("Unable to delete temporary input file" + ex.getMessage());
		}
	}

	private void loadArguments() {
		if (StringUtils.isBlank(this.url)) {
			return;
		}
		final String content;
		File file = new File(this.url);
		if (file.exists() && file.isFile()) {
			try {
				content = FileUtils.readFileToString(file);
			}
			catch (IOException ex) {
				// LOG.error("Unable to read file: " + this.url, e);
				return;
			}
		}
		else if (this.cliHelper.isValidURL(this.url)) {
			try {
				content = RemoteUrl.urlToString(this.url, null);
			}
			catch (Exception ex) {
				// LOG.error("Unable to read url: " + this.url, e);
				return;
			}
		}
		else {
			return;
		}

		if (StringUtils.isBlank(content)) {
			return;
		}

		JsonNode node = null;

		if (this.cliHelper.isValidJson(content)) {
			try {
				node = Json.mapper().readTree(content.getBytes());
			}
			catch (IOException ex) {
				// LOG.error("Unable to deserialize json from: " + this.url, e);
				node = null;
			}
		}
		else if (this.cliHelper.isValidYaml(content)) {
			try {
				node = Yaml.mapper().readTree(content.getBytes());
			}
			catch (IOException ex) {
				// LOG.error("Unable to deserialize yaml from: " + this.url, e);
				node = null;
			}
		}

		if (node == null) {
			return;
		}

		final Map<String, Object> optionValueMap = this.cliHelper.createOptionValueMap(node);
		try {
			BeanUtils.populate(this, optionValueMap);
		}
		catch (Exception ex) {
			// LOG.error("Error setting values to object.", e);
		}
	}

}

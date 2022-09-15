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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.google.common.io.Resources;
import io.swagger.codegen.v3.CodegenArgument;
import io.swagger.codegen.v3.CodegenConfig;
import io.swagger.codegen.v3.CodegenConfigLoader;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.BooleanSchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.util.SchemaTypeUtil;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.Argument;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CliHelper {

	static String loadResourceOAS3File() {
		URL url = Resources.getResource("configuration/oas3.yaml");
		try {
			return Resources.toString(url, Charsets.UTF_8);
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	static boolean containsOptionExtensions(Map<String, Object> extensions) {
		if (extensions == null) {
			return false;
		}
		final Object option = extensions.get("x-option");
		if (option != null && StringUtils.isNotBlank(option.toString())) {
			return true;
		}
		return false;
	}

	public static String getCommand(String schemaName, Schema schema) {
		if (schema.getExtensions() != null && !schema.getExtensions().isEmpty()
				&& schema.getExtensions().get("x-command") != null) {
			return schema.getExtensions().get("x-command").toString();
		}
		else {
			return schemaName.toLowerCase();
		}
	}

	public static String[] getArguments(Map<String, Object> extensions) {
		if (extensions.get("x-short-version") != null
				&& StringUtils.isNotBlank(extensions.get("x-short-version").toString())) {
			return new String[] { extensions.get("x-short-version").toString(), extensions.get("x-option").toString() };
		}
		return new String[] { extensions.get("x-option").toString() };
	}

	public static String[] getArguments(CodegenArgument codegenArgument) {
		List<String> options = new ArrayList<>();
		if (StringUtils.isNotBlank(codegenArgument.getOption())) {
			options.add(codegenArgument.getOption());
		}
		if (StringUtils.isNotBlank(codegenArgument.getShortOption())) {
			options.add(codegenArgument.getShortOption());
		}
		return options.toArray(new String[options.size()]);
	}

	public static String detectCommand(String[] args) {
		if (args == null || args.length == 0) {
			return null;
		}
		String command = args[0];
		if (StringUtils.isBlank(command) || command.startsWith("-")) {
			return null;
		}
		return command;
	}

	public static String detectlanguage(String[] args) {
		if (args == null || args.length == 0) {
			return null;
		}
		boolean langFlatFound = false;
		String language = null;
		for (String argument : args) {
			argument = argument.trim();
			if (langFlatFound) {
				if (argument.startsWith("-")) {
					return null;
				}
				return argument;
			}
			if ("-l".equalsIgnoreCase(argument) || "--lang".equalsIgnoreCase(argument)) {
				langFlatFound = true;
				continue;
			}
			if (argument.startsWith("-l") && argument.length() > 2) {
				return argument.substring(2);
			}
		}
		return language;
	}

	public static Class getClass(Schema property) {
		if (property instanceof BooleanSchema) {
			return Boolean.class;
		}
		return String.class;
	}

	Object getDefault(Schema property) {
		if (property instanceof BooleanSchema) {
			return Boolean.TRUE;
		}
		return null;
	}

	public static Map<String, Object> createOptionValueMap(Schema schema, Map<String, Object> inputArgs) {
		if (inputArgs == null || inputArgs.isEmpty()) {
			return null;
		}
		final Map<String, Schema> properties = schema.getProperties();
		if (properties == null || properties.isEmpty()) {
			return null;
		}
		final Map<String, Object> optionValueMap = new HashMap<>();
		for (String propertyName : properties.keySet()) {
			final Schema property = properties.get(propertyName);
			final Map<String, Object> extensions = property.getExtensions();
			if (extensions == null || extensions.isEmpty()) {
				continue;
			}
			Object value = null;
			if (extensions.get("x-option") != null) {
				String option = fixOptionName(extensions.get("x-option").toString());
				value = inputArgs.get(option);
			}
			else {
				continue;
			}
			if (value == null) {
				continue;
			}
			if (property instanceof BooleanSchema) {
				optionValueMap.put(propertyName, Boolean.valueOf(value.toString()));
			}
			else if (property instanceof IntegerSchema) {
				if (SchemaTypeUtil.INTEGER64_FORMAT.equals(property.getFormat())) {
					optionValueMap.put(propertyName, Long.valueOf(value.toString()));
				}
				else {
					optionValueMap.put(propertyName, Integer.valueOf(value.toString()));
				}
			}
			else if (property instanceof NumberSchema) {
				if (SchemaTypeUtil.FLOAT_FORMAT.equals(property.getFormat())) {
					optionValueMap.put(propertyName, Float.valueOf(value.toString()));
				}
				else {
					optionValueMap.put(propertyName, Double.valueOf(value.toString()));
				}
			}
			else if (property instanceof ArraySchema) {
				String inputElements = value.toString().replace("[", StringUtils.EMPTY).replace("]", StringUtils.EMPTY)
						.replace(" ", StringUtils.EMPTY);
				final List<String> values = new ArrayList<>(Arrays.asList(inputElements.split(",")));
				optionValueMap.put(propertyName, values);
			}
			else {
				optionValueMap.put(propertyName, value);
			}
		}
		return optionValueMap;
	}

	public Map<String, Object> createOptionValueMap(JsonNode node) {
		final Map<String, Object> optionValueMap = new HashMap<>();
		Iterator<String> fieldNames = node.fieldNames();
		while (fieldNames.hasNext()) {
			String argument = fieldNames.next();
			JsonNode valueNode = node.findValue(argument);
			if (valueNode.isBoolean()) {
				optionValueMap.put(argument, valueNode.booleanValue());
			}
			else if (valueNode.isShort() || valueNode.isInt()) {
				optionValueMap.put(argument, valueNode.intValue());
			}
			else if (valueNode.isLong()) {
				optionValueMap.put(argument, valueNode.longValue());
			}
			else if (valueNode.isFloat()) {
				optionValueMap.put(argument, valueNode.floatValue());
			}
			else if (valueNode.isDouble()) {
				optionValueMap.put(argument, valueNode.doubleValue());
			}
			else if (valueNode.isArray()) {
				String inputElements = valueNode.toString().replace("[", StringUtils.EMPTY)
						.replace("]", StringUtils.EMPTY).replace("\"", StringUtils.EMPTY)
						.replace(" ", StringUtils.EMPTY);
				final List<String> values = new ArrayList<>(Arrays.asList(inputElements.split(",")));
				optionValueMap.put(argument, values);
			}
			else {
				optionValueMap.put(argument, valueNode.toString().replace("\"", StringUtils.EMPTY));
			}
		}
		return optionValueMap;
	}

	public static String fixOptionName(String option) {
		option = option.substring(countDashes(option));
		return option.replace("-", "_");
	}

	private static int countDashes(String option) {
		for (int i = 0; i < option.length(); i++) {
			if (option.charAt(i) != '-') {
				return i;
			}
		}
		return 0;
	}

	public boolean isValidJson(String content) {

		if (StringUtils.isBlank(content)) {
			return false;
		}
		try {
			new ObjectMapper().readTree(content);
			return true;
		}
		catch (IOException ex) {
			return false;
		}
	}

	public boolean isValidYaml(String content) {
		if (StringUtils.isBlank(content)) {
			return false;
		}
		try {
			new YAMLMapper().readTree(content);
			return true;
		}
		catch (IOException ex) {
			return false;
		}
	}

	public boolean isValidURL(String urlStr) {
		if (StringUtils.isBlank(urlStr)) {
			return false;
		}
		try {
			URI uri = new URI(urlStr);
			return uri.getScheme().toLowerCase().startsWith("http");
		}
		catch (Exception ex) {
			return false;
		}
	}

	public static boolean isValidString(String content) {
		return StringUtils.isBlank(content) || content.equalsIgnoreCase(null);
	}

	static void setCommandObject(String args) {
		String[] codeGenerationArgs = args.split("\\s+");
		String oas3 = loadResourceOAS3File();
		if (StringUtils.isBlank(oas3)) {
			throw new InvalidArgException("Could not load oas3 resource file.");
		}

		final OpenAPI openAPI = new OpenAPIV3Parser().readContents(oas3, null, null).getOpenAPI();
		final Map<String, Schema> schemaMap = openAPI.getComponents().getSchemas();
		final Set<String> schemaNames = schemaMap.keySet();

		final ArgumentParser codegenParser = ArgumentParsers.newFor("swagger-codegen").build();
		final Subparsers subparsers = codegenParser.addSubparsers().title("commands").help("additional help")
				.metavar("Command");

		final Map<String, Schema> commandMap = new HashMap<>();
		List<CodegenArgument> codegenArguments = null;

		for (String schemaName : schemaNames) {

			final Schema<?> schema = schemaMap.get(schemaName);
			final String command = getCommand(schemaName, schema);
			final Map<String, Schema> schemaProperties = schema.getProperties();
			final Subparser parser = subparsers.addParser(command).help(command);
			commandMap.put(command, schema);
			for (String propertyName : schemaProperties.keySet()) {
				final Schema<?> property = schemaProperties.get(propertyName);
				final Map<String, Object> extensions = property.getExtensions();

				String[] arguments = getArguments(extensions);
				final Argument argument = parser.addArgument(arguments).type(getClass(property))
						.help(property.getDescription()).metavar(StringUtils.EMPTY);

				if (property instanceof BooleanSchema) {
					argument.nargs("?").setConst(true);
				}
				else if (property instanceof ArraySchema) {
					argument.nargs("*");
				}
			}
			String language = detectlanguage(codeGenerationArgs);
			if (StringUtils.isNotBlank(language)) {
				CodegenConfig config = CodegenConfigLoader.forName(language);
				codegenArguments = config.readLanguageArguments();
				if (CollectionUtils.isNotEmpty(codegenArguments)) {
					for (CodegenArgument codegenArgument : codegenArguments) {
						String[] arguments = getArguments(codegenArgument);
						Class<?> codeGenArgType = "boolean".equalsIgnoreCase(codegenArgument.getType()) ? Boolean.class
								: String.class;
						final Argument argument = parser.addArgument(arguments).type(codeGenArgType)
								.help(codegenArgument.getDescription()).metavar(StringUtils.EMPTY);
						if (codegenArgument.getType().equalsIgnoreCase("boolean")) {
							argument.nargs("?").setConst(true);
						}
						else if (codegenArgument.getArray() != null && codegenArgument.getArray()) {
							argument.nargs("*");
						}
					}
				}
			}
		}
		final Map<String, Object> inputArgs = new HashMap<>();
		try {
			codegenParser.parseArgs(codeGenerationArgs, inputArgs);
		}
		catch (ArgumentParserException ex) {
			codegenParser.handleError(ex);
			throw new InvalidArgException("Invalid Arg: " + ex.getMessage());
		}
		final String userInputCommand = detectCommand(codeGenerationArgs);
		if (userInputCommand == null) {
			throw new InvalidArgException("No command found.");

		}
		final Schema<?> commandSchema = commandMap.get(userInputCommand);
		if (commandSchema == null) {
			throw new InvalidArgException("There are not schema related to command " + userInputCommand);

		}
		final Map<String, Object> extensions = commandSchema.getExtensions();

		if (extensions == null || extensions.isEmpty() || extensions.get("x-class-name") == null) {
			throw new InvalidArgException("Extensions are required to run command. i.e: 'x-class-name'");
		}
		final String className = extensions.get("x-class-name").toString();
		final Class<?> xClassName;
		final Object commandObject;
		try {
			xClassName = Class.forName(className);
			commandObject = xClassName.newInstance();
			final Map<String, Object> optionValueMap = createOptionValueMap(commandSchema, inputArgs);
			BeanUtils.populate(commandObject, optionValueMap);
			if (CollectionUtils.isNotEmpty(codegenArguments) && commandObject instanceof Generate) {
				codegenArguments = codegenArguments.stream().filter(codegenArgument -> {
					final String option = fixOptionName(codegenArgument.getOption());
					final String optionValue = String.valueOf(inputArgs.get(option));

					if (isValidString(optionValue)) {
						codegenArgument.setValue(optionValue);
						return true;
					}
					else {
						return false;
					}
				}).collect(Collectors.toList());

				Generate generateCommand = (Generate) commandObject;
				generateCommand.setCodegenArguments(codegenArguments);
				generateCommand.generateSwaggerCode();
			}
		}
		catch (ClassNotFoundException | IllegalAccessException | InstantiationException
				| InvocationTargetException ex) {
			throw new InvalidArgException("Could not load class " + className + "for command" + userInputCommand
					+ " and message is " + ex.getMessage());

		}
	}

}
package io.spring.initializr.generator.spring.codegen;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import io.spring.initializr.generator.language.SourceStructure;
import io.spring.initializr.generator.project.CustomProjectDescription;
import io.spring.initializr.generator.project.contributor.ProjectContributor;

/**
 * {@link ProjectContributor} for the project's structure specified in
 * {@code Swagger.yaml} file.
 *
 * @author Sayeed
 *
 */
public class CodegenContributor implements ProjectContributor {

	private final CustomProjectDescription description;

	CliHelper cliHelper = new CliHelper();

	public CodegenContributor(CustomProjectDescription description) {
		this.description = description;
	}

	@Override
	public void contribute(Path projectRoot) throws IOException {
		// TODO
		// Application name = this.description.getApplicationName(); eg:
		// ProductApplication
		// Artifacte Id = this.description.getArtifactId(); eg: product
		// group id = this.description.getGroupId(); eg:com.litmus
		// language = this.description.getLanguage().id(); eg:java
		// language version = this.description.getLanguage().jvmVersion(); eg:17
		// package name = this.description.getPackageName(); eg:com.litmus.product
		// spring boot version = this.description.getPlatformVersion() eg:2.7.2

		SourceStructure sourceStructure = this.description.getBuildSystem().getMainSource(projectRoot,
				this.description.getLanguage());

		// resource directory sourceStructure.getResourcesDirectory();
		// C:\Users\sayeed\AppData\Local\Temp\project-13060793765327664540\product\src\main\resources
		// source directory sourceStructure.getSourcesDirectory();
		// //C:\Users\sayeed\AppData\Local\Temp\project-13060793765327664540\product\src\main\java

		writeSwaggerFile(projectRoot);
	}

	private void writeSwaggerFile(Path projectRoot) {
		// TODO Code for writing swagger file

		System.out.println("swagger file" + this.description.getSwaggerFile());

		String args = generateCodeGenerationArgs(projectRoot);
		cliHelper.setCommandObject(args);
		System.out.println("swagger code generation completed...");
		// if (commandObject instanceof Runnable) {
		// new Thread(((Runnable) commandObject)).start();
		// }
	}

	private String generateCodeGenerationArgs(Path projectRoot) {
		StringBuilder codeGenArgs = new StringBuilder();
		String args = null;
		codeGenArgs.append("generate");
		if (cliHelper.isValidString(description.getGroupId()))
			codeGenArgs.append(" --group-id ").append(description.getGroupId());
		if (cliHelper.isValidString(description.getArtifactId()))
			codeGenArgs.append(" --artifact-id ").append(description.getArtifactId());

		String basePackage = description.getGroupId() + "." + description.getArtifactId();
		codeGenArgs.append(" --api-package ").append(basePackage + ".api");
		codeGenArgs.append(" --model-package ").append(basePackage + ".model");
		codeGenArgs.append(" --invoker-package ").append(basePackage);
		codeGenArgs.append(" --config-package ").append(basePackage + ".configuration");
		codeGenArgs.append(" --controller-package ").append(basePackage + ".controller");
		codeGenArgs.append(" --controllerimpl-package ").append(basePackage + ".controller.impl");
		codeGenArgs.append(" --exception-package ").append(basePackage + ".exception");
		//codeGenArgs.append(" --library spring-cloud");
		setFramework(codeGenArgs);
		codeGenArgs.append(" -o ").append(projectRoot);

		setInputFile(codeGenArgs);
		System.out.println("stringBuilder:  " + codeGenArgs);

		args = codeGenArgs.toString();
		return args;
	}

	private void setFramework(StringBuilder codeGenArgs) {
		String framework = null;
		switch (this.description.getLanguage().toString()) {

		case "jaxrs-jersey":
			framework = "jaxrs-jersey";
			break;
		case "micronaut":
			framework = "micronaut";
			break;
		case "kotlin-server":
			framework = "kotlin-server";
			break;
		case "kotlin-client":
			framework = "kotlin-client";
			break;
		default:
			framework = "spring";
		}
		codeGenArgs.append(" -l ").append(framework);
	}

	private void setInputFile(StringBuilder codeGenArgs) {
		String prefix = "temp";
		byte[] bytes = new byte[0];
		try {
			bytes = this.description.getSwaggerFile().getBytes();
			Path tempDirWithPrefix = Files.createTempDirectory(prefix);
			Files.write(Paths.get(tempDirWithPrefix + this.description.getSwaggerFile().getOriginalFilename()), bytes);
			codeGenArgs.append(" -i ").append(
					Paths.get(tempDirWithPrefix + this.description.getSwaggerFile().getOriginalFilename()).toString());
		}
		catch (IOException ex) {
			throw new InvalidArgException("Error in setting inputFile: " + ex.getMessage());
		}
	}
//	public void setCommandObject(String args) {
//	String[] codeGenerationArgs = args.split("\\s+");
//	String oas3 = cliHelper.loadResourceOAS3File();
//	if (StringUtils.isBlank(oas3)) {
//		throw new InvalidArgException("Could not load oas3 resource file.");
//	}
//
//	final OpenAPI openAPI = new OpenAPIV3Parser().readContents(oas3, null, null).getOpenAPI();
//	final Map<String, Schema> schemaMap = openAPI.getComponents().getSchemas();
//	final Set<String> schemaNames = schemaMap.keySet();
//
//	final ArgumentParser codegenParser = ArgumentParsers.newFor("swagger-codegen").build();
//	final Subparsers subparsers = codegenParser.addSubparsers().title("commands").help("additional help")
//			.metavar("Command");
//
//	final Map<String, Schema> commandMap = new HashMap<>();
//	List<CodegenArgument> codegenArguments = null;
//
//	for (String schemaName : schemaNames) {
//
//		final Schema<?> schema = schemaMap.get(schemaName);
//		final String command = cliHelper.getCommand(schemaName, schema);
//		final Map<String, Schema> schemaProperties = schema.getProperties();
//		final Subparser parser = subparsers.addParser(command).help(command);
//		commandMap.put(command, schema);
//		for (String propertyName : schemaProperties.keySet()) {
//			final Schema<?> property = schemaProperties.get(propertyName);
//			final Map<String, Object> extensions = property.getExtensions();
//
//			String[] arguments = cliHelper.getArguments(extensions);
//			final Argument argument = parser.addArgument(arguments).type(cliHelper.getClass(property))
//					.help(property.getDescription()).metavar(StringUtils.EMPTY);
//
//			if (property instanceof BooleanSchema) {
//				argument.nargs("?").setConst(true);
//			}
//			else if (property instanceof ArraySchema) {
//				argument.nargs("*");
//			}
//		}
//		String language = cliHelper.detectlanguage(codeGenerationArgs);
//		if (StringUtils.isNotBlank(language)) {
//			CodegenConfig config = CodegenConfigLoader.forName(language);
//			codegenArguments = config.readLanguageArguments();
//			if (CollectionUtils.isNotEmpty(codegenArguments)) {
//				for (CodegenArgument codegenArgument : codegenArguments) {
//					String[] arguments = cliHelper.getArguments(codegenArgument);
//					Class<?> codeGenArgType = "boolean".equalsIgnoreCase(codegenArgument.getType()) ? Boolean.class
//							: String.class;
//					final Argument argument = parser.addArgument(arguments).type(codeGenArgType)
//							.help(codegenArgument.getDescription()).metavar(StringUtils.EMPTY);
//					if (codegenArgument.getType().equalsIgnoreCase("boolean")) {
//						argument.nargs("?").setConst(true);
//					}
//					else if (codegenArgument.getArray() != null && codegenArgument.getArray()) {
//						argument.nargs("*");
//					}
//				}
//			}
//		}
//	}
//	final Map<String, Object> inputArgs = new HashMap<>();
//	try {
//		codegenParser.parseArgs(codeGenerationArgs, inputArgs);
//	}
//	catch (ArgumentParserException e) {
//		codegenParser.handleError(e);
//		throw new InvalidArgException("Invalid Arg: " + e.getMessage());
//	}
//	final String userInputCommand = cliHelper.detectCommand(codeGenerationArgs);
//	if (userInputCommand == null) {
//		throw new InvalidArgException("No command found.");
//
//	}
//	final Schema<?> commandSchema = commandMap.get(userInputCommand);
//	if (commandSchema == null) {
//		throw new InvalidArgException("There are not schema related to command " + userInputCommand);
//
//	}
//	final Map<String, Object> extensions = commandSchema.getExtensions();
//
//	if (extensions == null || extensions.isEmpty() || extensions.get("x-class-name") == null) {
//		throw new InvalidArgException("Extensions are required to run command. i.e: 'x-class-name'");
//	}
//	final String className = extensions.get("x-class-name").toString();
//	final Class<?> xClassName;
//	final Object commandObject;
//	try {
//		xClassName = Class.forName(className);
//		commandObject = xClassName.newInstance();
//		final Map<String, Object> optionValueMap = cliHelper.createOptionValueMap(commandSchema, inputArgs);
//		BeanUtils.populate(commandObject, optionValueMap);
//		if (CollectionUtils.isNotEmpty(codegenArguments) && commandObject instanceof Generate) {
//			codegenArguments = codegenArguments.stream().filter(codegenArgument -> {
//				final String option = cliHelper.fixOptionName(codegenArgument.getOption());
//				final String optionValue = String.valueOf(inputArgs.get(option));
//
//				if (cliHelper.isValidString(optionValue)) {
//					codegenArgument.setValue(optionValue);
//					return true;
//				}
//				else {
//					return false;
//				}
//			}).collect(Collectors.toList());
//
//			Generate generateCommand = (Generate) commandObject;
//			generateCommand.setCodegenArguments(codegenArguments);
//			generateCommand.generateSwaggerCode();
//		}
//	}
//	catch (ClassNotFoundException | IllegalAccessException | InstantiationException
//			| InvocationTargetException ex) {
//		throw new InvalidArgException("Could not load class " + className + "for command" + userInputCommand
//				+ " and message is " + ex.getMessage());
//
//	}
//}
}
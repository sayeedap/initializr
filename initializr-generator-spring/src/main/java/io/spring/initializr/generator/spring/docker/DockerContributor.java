package io.spring.initializr.generator.spring.docker;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

import io.spring.initializr.generator.project.contributor.ProjectContributor;

/**
 * @author Sayeed
 *
 */
public class DockerContributor implements ProjectContributor {

	private final DockerFile dockerFile;

	public DockerContributor() {
		super();
		this.dockerFile = new DockerFile();

	}

	public DockerContributor(DockerFile description) {
		super();
		this.dockerFile = description;
	}

	@Override
	public void contribute(Path projectRoot) throws IOException {
		Path dockerFile = Files.createFile(projectRoot.resolve("DockerFile"));
		try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(dockerFile))) {
			writeImage(writer);
			writePort(writer);
			writeCopyCommand(writer);
			writeCommand(writer);
		}

	}

	private void writeCommand(PrintWriter writer) throws IOException {
		writer.print(dockerFile.getCommand().getType()+ " [\"" + dockerFile.getCommand().getCommand() + "\",");
		writer.print(dockerFile.getCommand().getParameter().stream().map((parameter) -> " \"" + parameter + "\" ")
				.collect(Collectors.joining(", ")));

		writer.println(" ]");

	}

	private void writeCopyCommand(PrintWriter writer) throws IOException {
		writer.println("COPY " + dockerFile.getCopy().getSource() + " " + dockerFile.getCopy().getDestination());
	}

	private void writePort(PrintWriter writer) throws IOException {
		if (dockerFile.getPort() == 0) {
			return;
		}
		writer.println("EXPOSE " + dockerFile.getPort());
	}

	private void writeImage(PrintWriter writer) throws IOException {
		writer.println("FROM " + dockerFile.getImage().getName() + ":" + dockerFile.getImage().getTag());

	}

}

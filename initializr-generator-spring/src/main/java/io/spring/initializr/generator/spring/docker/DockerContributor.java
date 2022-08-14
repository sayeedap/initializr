package io.spring.initializr.generator.spring.docker;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import io.spring.initializr.generator.project.CustomProjectDescription;
import io.spring.initializr.generator.project.contributor.ProjectContributor;

public class DockerContributor implements ProjectContributor  {


	
	private final DockerFileDeclaration dockerFileDeclaration;
	



	public DockerContributor() {super();
		this.dockerFileDeclaration = new DockerFileDeclaration();
		
		// TODO Auto-generated constructor stub
//		this.dockerFileDeclaration=description2;
	}

	public DockerContributor(DockerFileDeclaration description) {
		super();
		this.dockerFileDeclaration = description;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void contribute(Path projectRoot) throws IOException {
		Path dockerFile = Files.createFile(projectRoot.resolve("DockerFile"));
//		System.out.println("::::" + this.description.getApplicationName());
		try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(dockerFile))) {
			write(writer);
		}

	}

	private void write(PrintWriter writer) {
		writer.print("sayeeed");

	}


}

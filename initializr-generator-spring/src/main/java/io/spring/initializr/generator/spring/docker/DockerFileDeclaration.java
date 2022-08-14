package io.spring.initializr.generator.spring.docker;

import java.util.List;

public class DockerFileDeclaration {

	private DockerBaseImage DockerBaseImage;

	private DockerCopyCommnad dockerCopyCommnad;

	private DockerEntryPoint dockerEntryPoint;

	public DockerBaseImage getDockerBaseImage() {
		return DockerBaseImage;
	}

	public void setDockerBaseImage(DockerBaseImage dockerBaseImage) {
		DockerBaseImage = dockerBaseImage;
	}

	public DockerCopyCommnad getDockerCopyCommnad() {
		return dockerCopyCommnad;
	}

	public void setDockerCopyCommnad(DockerCopyCommnad dockerCopyCommnad) {
		this.dockerCopyCommnad = dockerCopyCommnad;
	}

	public DockerEntryPoint getDockerEntryPoint() {
		return dockerEntryPoint;
	}

	public void setDockerEntryPoint(DockerEntryPoint dockerEntryPoint) {
		this.dockerEntryPoint = dockerEntryPoint;
	}

}

class DockerBaseImage {

	private String imageName;

	private String tagName;

	public DockerBaseImage(String imageName, String tagName) {
		this.imageName = imageName;
		this.tagName = tagName;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

}

class DockerCopyCommnad {

	private String source;

	private String destination;

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

}

class DockerEntryPoint {

	private String command;

	private List<String> parameter;

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public List<String> getParameter() {
		return parameter;
	}

	public void setParameter(List<String> parameter) {
		this.parameter = parameter;
	}

}

package io.spring.initializr.generator.spring.docker;

/**
 * The {@link DockerFile} contains all the commands a user could call on the command line
 * to assemble an image
 *
 * @author Sayeed
 */
public class DockerFile {

	private Image image;

	private Copy copy;

	private Command command;

	private int port;

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public Copy getCopy() {
		return copy;
	}

	public void setCopy(Copy copy) {
		this.copy = copy;
	}

	public Command getCommand() {
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}

package io.spring.initializr.generator.spring.docker;

/**
 * The {@link Copy} instruction copies new files or directories from {@link source} and
 * adds them to the filesystem of the container at the path {@link destination}
 *
 * @author Sayeed AP
 */
public class Copy {

	private String source;

	private String destination;

	public Copy(String source, String destination) {
		super();
		this.source = source;
		this.destination = destination;
	}

	public String getSource() {
		return source;
	}

	public String getDestination() {
		return destination;
	}

}
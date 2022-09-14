package io.spring.initializr.generator.spring.docker;

/**
 * Sets the Base Image for subsequent instructions
 * 
 * @author Sayeed AP
 */
public class Image {

	private String name;

	private String tag = "latest";

	public Image(String name, String tag) {
		this.name = name;
		this.tag = tag;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * @param tag the tag to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

}
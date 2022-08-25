package io.spring.initializr.generator.language.java;

import java.util.List;

public class JavaReturnType {

	private final String returnType ;

	private final List<String> generics;

	public JavaReturnType(String returnType, List<String> generics) {
		super();
		this.returnType = returnType;
		this.generics = generics;
	}
	
	public JavaReturnType(String returnType) {
		super();
		this.returnType = returnType;
		this.generics = null;
	}

	/**
	 * @return the returnType
	 */
	public String getReturnType() {
		return returnType;
	}

	/**
	 * @return the generics
	 */
	public List<String> getGenerics() {
		return generics;
	}

}

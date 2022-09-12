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

package io.spring.initializr.generator.language.java;

import java.util.List;

/**
 * Class for java return type.
 *
 * @author Sayeed A
 *
 */
public class JavaReturnType {

	private final String returnType;

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

	public String getReturnType() {
		return this.returnType;
	}

	public List<String> getGenerics() {
		return this.generics;
	}

}

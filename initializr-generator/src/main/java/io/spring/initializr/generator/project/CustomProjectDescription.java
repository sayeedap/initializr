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

package io.spring.initializr.generator.project;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

/**
 * A custom {@link ProjectDescription} to convey the additional flags to
 * contributors.
 *
 * @author Stephane Nicoll
 * @author Sayeed
 */
public class CustomProjectDescription extends MutableProjectDescription {

	private Boolean dockerFlag = true;

	private List<String> projectComponents = new ArrayList<>();
	
	private MultipartFile swaggerFile;

	public CustomProjectDescription() {
	}

	CustomProjectDescription(CustomProjectDescription source) {
		super(source);
		this.dockerFlag=source.getDockerFlag();
		this.projectComponents=source.getProjectComponents();
		this.swaggerFile=source.getSwaggerFile();
	}

	@Override
	public CustomProjectDescription createCopy() {
		return new CustomProjectDescription(this);
	}

	public Boolean getDockerFlag() {
		return dockerFlag;
	}

	public void setDockerFlag(Boolean dockerFlag) {
		this.dockerFlag = dockerFlag;
	}

	public List<String> getProjectComponents() {
		return projectComponents;
	}

	public void setProjectComponents(List<String> projectComponents) {
		this.projectComponents = projectComponents;
	}

	public MultipartFile getSwaggerFile() {
		return swaggerFile;
	}

	public void setSwaggerFile(MultipartFile swaggerFile) {
		this.swaggerFile = swaggerFile;
	}
	
	

}

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

package io.spring.initializr.generator.condition;

import io.spring.initializr.generator.project.CustomProjectDescription;

import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * OnComponentsCondition.
 *
 * @author Sayeed AP
 *
 */
public class OnComponentsCondition extends CustomProjectGenerationCondition {

	@Override
	protected boolean matches(CustomProjectDescription description, ConditionContext context,
			AnnotatedTypeMetadata metadata) {
		String componentTrip = (String) metadata.getAllAnnotationAttributes(ConditionalOnComponents.class.getName())
				.getFirst("value");

		if (description.getProjectComponents().isEmpty()) {
			return true;
		}

		if (description.getProjectComponents().contains(componentTrip)) {

			return true;
		}

		return false;
	}

}

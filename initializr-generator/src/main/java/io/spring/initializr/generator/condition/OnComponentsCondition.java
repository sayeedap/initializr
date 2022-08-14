package io.spring.initializr.generator.condition;

import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import io.spring.initializr.generator.project.CustomProjectDescription;

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

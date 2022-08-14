package io.spring.initializr.generator.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import io.spring.initializr.generator.project.CustomProjectDescription;
import io.spring.initializr.generator.project.ProjectDescription;

/**
 * Base class for project generation {@link Condition Conditions} that rely on the state
 * of the {@link ProjectDescription}.
 *
 * @author Sayeed
 */
public abstract class CustomProjectGenerationCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		CustomProjectDescription description = context.getBeanFactory().getBean(CustomProjectDescription.class);
		return matches(description, context, metadata);
	}

	protected abstract boolean matches(CustomProjectDescription description, ConditionContext context,
			AnnotatedTypeMetadata metadata);

}
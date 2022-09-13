package io.spring.initializr.generator.condition;

import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.web.multipart.MultipartFile;

import io.spring.initializr.generator.project.CustomProjectDescription;

/**
 * @author Sayeed
 *
 */
public class OnSwaggerFileCondition extends CustomProjectGenerationCondition {

	@Override
	protected boolean matches(CustomProjectDescription description, ConditionContext context,
			AnnotatedTypeMetadata metadata) {
		String condition = (String) metadata.getAllAnnotationAttributes(ConditionalOnSwaggerFile.class.getName())
				.getFirst("value");
		if (condition.equals("hasSwaggerFile")) {
			return checkSwaggerFile(description.getSwaggerFile());
		} else {
			return checkNotHaveSwaggerFile(description.getSwaggerFile());
		}

	}

	private boolean checkNotHaveSwaggerFile(MultipartFile swaggerFile) {
		if (swaggerFile == null || swaggerFile.isEmpty())
			return true;
		else
			return false;
	}

	private boolean checkSwaggerFile(MultipartFile swaggerFile) {
		if (swaggerFile != null && !swaggerFile.isEmpty())
			return true;
		else
			return false;
	}

}

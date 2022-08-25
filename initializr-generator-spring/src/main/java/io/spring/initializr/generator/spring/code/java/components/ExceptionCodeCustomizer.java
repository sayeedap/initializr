package io.spring.initializr.generator.spring.code.java.components;

import org.springframework.core.Ordered;

import io.spring.initializr.generator.language.TypeDeclaration;

/**
 * @author Sayeed
 *
 */
@FunctionalInterface
public interface ExceptionCodeCustomizer extends Ordered {

	void customize();

	@Override
	default int getOrder() {
		return 0;
	}

}

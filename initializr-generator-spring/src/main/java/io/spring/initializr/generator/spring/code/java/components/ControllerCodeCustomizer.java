package io.spring.initializr.generator.spring.code.java.components;

import org.springframework.core.Ordered;

import io.spring.initializr.generator.language.TypeDeclaration;

/**
 * @author Sayeed A
 * @param <T>
 */
@FunctionalInterface
public interface ControllerCodeCustomizer<T extends TypeDeclaration> extends Ordered {

	void customize(T typeDeclaration);

	@Override
	default int getOrder() {
		return 0;
	}

}

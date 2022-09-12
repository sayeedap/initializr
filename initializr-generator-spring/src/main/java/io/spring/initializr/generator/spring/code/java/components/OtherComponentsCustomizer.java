package io.spring.initializr.generator.spring.code.java.components;

import org.springframework.core.Ordered;

import io.spring.initializr.generator.language.TypeDeclaration;

/**
 * @author Sayeed
 * @param <T>
 */
@FunctionalInterface
public interface OtherComponentsCustomizer<T extends TypeDeclaration> extends Ordered {

	void customize();

	@Override
	default int getOrder() {
		return 0;
	}

}

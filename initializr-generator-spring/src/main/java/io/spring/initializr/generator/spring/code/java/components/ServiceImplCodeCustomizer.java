package io.spring.initializr.generator.spring.code.java.components;

import org.springframework.core.Ordered;

import io.spring.initializr.generator.language.TypeDeclaration;

/**
 * @author Sayeed
 *
 * @param <T>
 */
@FunctionalInterface
public interface ServiceImplCodeCustomizer<T extends TypeDeclaration> extends Ordered {

	void customize(T typeDeclaration);

	@Override
	default int getOrder() {
		return 0;
	}
}

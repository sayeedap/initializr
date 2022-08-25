package io.spring.initializr.generator.spring.code.components;

import org.springframework.core.Ordered;

import io.spring.initializr.generator.language.TypeDeclaration;

@FunctionalInterface
public interface RepositoryCodeCustomizer<T extends TypeDeclaration> extends Ordered {

	void customize(T typeDeclaration);

	@Override
	default int getOrder() {
		return 0;
	}

}

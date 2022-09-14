package io.spring.initializr.generator.spring.code.java.components;

import org.springframework.core.Ordered;

/**
 * @author Sayeed AP
 *
 */
@FunctionalInterface
public interface ConfigurationCodesCustomizer extends Ordered {

	void customize();

	@Override
	default int getOrder() {
		return 0;
	}

}

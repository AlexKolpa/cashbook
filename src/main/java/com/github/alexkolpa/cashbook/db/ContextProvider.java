package com.github.alexkolpa.cashbook.db;

import javax.inject.Inject;
import javax.inject.Provider;

import java.util.function.Consumer;
import java.util.function.Function;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE, onConstructor = @__(@Inject))
public class ContextProvider {

	private final Provider<DSLContext> contextProvider;

	public void transaction(Consumer<DSLContext> consumer) {
		try (DSLContext context = contextProvider.get()) {
			context.transaction(configuration -> {
				DSLContext innerContext = DSL.using(configuration);
				consumer.accept(innerContext);
			});
		}
	}

	public <T> T transactionResult(Function<DSLContext, T> function) {
		try (DSLContext context = contextProvider.get()) {
			return context.transactionResult(configuration -> {
				DSLContext innerContext = DSL.using(configuration);
				return function.apply(innerContext);
			});
		}
	}
}

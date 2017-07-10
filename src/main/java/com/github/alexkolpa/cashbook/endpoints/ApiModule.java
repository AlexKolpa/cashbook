package com.github.alexkolpa.cashbook.endpoints;

import com.google.inject.AbstractModule;

public class ApiModule extends AbstractModule {
	@Override
	@SuppressWarnings("PointlessBinding")
	protected void configure() {
		bind(FlowResource.class);
		bind(RecurringFlowResource.class);
		bind(CategoriesResource.class);
		bind(SubCategoriesResource.class);
	}
}

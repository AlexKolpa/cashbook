package com.github.alexkolpa.cashbook.db;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.jooq.DSLContext;
import org.junit.rules.ExternalResource;

public class WithDB extends ExternalResource {

	private ContextProvider contextProvider;
	private final String fixtureDir;

	public WithDB() {
		this.fixtureDir = null;
	}

	public WithDB(String fixtureDir) {
		this.fixtureDir = fixtureDir;
	}

	@Override
	protected void before() throws IOException, SQLException {
		Properties properties = new Properties();
		properties.load(getClass().getResourceAsStream("/config.properties"));
		String url = properties.getProperty("jdbc.url");
		String user = properties.getProperty("jdbc.user");
		String pass = properties.getProperty("jdbc.pass");
		Injector injector = Guice.createInjector(new JooqModule(url, user, pass));
		DBMigrator migrator = injector.getInstance(DBMigrator.class);
		migrator.setLocations(fixtureDir);
		migrator.migrate(DBMigrator.Action.DROP_CREATE);
		contextProvider = injector.getInstance(ContextProvider.class);
	}

	public void transaction(Consumer<DSLContext> consumer) {
		contextProvider.transaction(consumer);
	}

	public <T> T transactionResult(Function<DSLContext, T> function) {
		return contextProvider.transactionResult(function);
	}

}

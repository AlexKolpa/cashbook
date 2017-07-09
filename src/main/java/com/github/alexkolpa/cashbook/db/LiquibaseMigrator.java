package com.github.alexkolpa.cashbook.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import com.google.inject.Inject;
import com.google.inject.Provider;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE, onConstructor = @__(@Inject))
public class LiquibaseMigrator {

	public enum Action {
		DROP_CREATE, UPDATE, NOP
	}

	private final Provider<Connection> connectionProvider;


	public void migrate(Action action, Collection<String> files) throws LiquibaseException, SQLException {
		//Early return
		if (action == Action.NOP) {
			return;
		}
		try (Connection connection = connectionProvider.get()) {
			switch (action) {
				case DROP_CREATE:
					drop(connection);
				case UPDATE:
					for (String fixture : files) {
						update(fixture, connection);
					}
					break;
				default:
					throw new IllegalArgumentException("Unknown action " + action);
			}
		}
	}

	private void drop(Connection connection) throws LiquibaseException, SQLException {
		Database database = getDatabaseForConnection(connection);

		new Liquibase((String) null, new ClassLoaderResourceAccessor(), database).dropAll();
	}

	private void update(String file, Connection connection) throws LiquibaseException, SQLException {
		Database database = getDatabaseForConnection(connection);
		new Liquibase(file, new ClassLoaderResourceAccessor(), database).update("");
	}

	private Database getDatabaseForConnection(Connection connection) throws DatabaseException {
		return DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
	}
}

package com.github.alexkolpa.cashbook.db;

import javax.inject.Provider;

import java.sql.Connection;
import java.sql.SQLException;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.jooq.ConnectionProvider;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;

public class JooqModule extends AbstractModule {

	private final ComboPooledDataSource pool;

	public JooqModule(String url, String username, String password) {
		this.pool = new ComboPooledDataSource();
		pool.setJdbcUrl(url);
		pool.setUser(username);
		pool.setPassword(password);
	}

	@Override
	protected void configure() {
	}

	@Provides
	public Connection getConnection() throws SQLException {
		return pool.getConnection();
	}

	@Provides
	public DSLContext getContext(Provider<Connection> connections) {
		ConnectionProvider connectionProvider = new ConnectionProvider() {
			@Override
			public Connection acquire() throws DataAccessException {
				try {
					Connection connection = connections.get();
					connection.setAutoCommit(false);
					return connection;
				}
				catch (SQLException e) {
					throw new DataAccessException("Exception while disabling auto-commit for connection.", e);
				}
			}

			@Override
			public void release(Connection connection) throws DataAccessException {
				try {
					connection.close();
				}
				catch (SQLException e) {
					throw new DataAccessException("Exception while releasing connection", e);
				}
			}
		};

		return DSL.using(connectionProvider, SQLDialect.POSTGRES_9_5);
	}
}

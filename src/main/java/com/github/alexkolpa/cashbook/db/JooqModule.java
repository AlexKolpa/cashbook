package com.github.alexkolpa.cashbook.db;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.extern.slf4j.Slf4j;
import org.jooq.ConnectionProvider;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;

@Slf4j
public class JooqModule extends AbstractModule {

	private final ComboPooledDataSource pool;

	public JooqModule(String url, String username, String password) {
		this.pool = new ComboPooledDataSource();
		pool.setJdbcUrl(Preconditions.checkNotNull(url));
		Optional.ofNullable(username).ifPresent(pool::setUser);
		Optional.ofNullable(password).ifPresent(pool::setPassword);
		log.info("Connecting with Postgres on {} as {}", url, username);
	}

	@Override
	protected void configure() {
	}

	@Provides
	public DataSource getDataSource() {
		return pool;
	}

	@Provides
	public DSLContext getContext() {
		ConnectionProvider connectionProvider = new ConnectionProvider() {
			@Override
			public Connection acquire() throws DataAccessException {
				try {
					Connection connection = pool.getConnection();
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

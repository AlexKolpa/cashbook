package com.github.alexkolpa.cashbook.db;

import javax.inject.Inject;
import javax.sql.DataSource;

import java.sql.SQLException;
import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;

@Slf4j
public class DBMigrator {

	private static final String SCHEMA = "public";
	private static final String LOCATION = "db/migration";

	public enum Action {
		DROP_CREATE, UPDATE, NOP
	}

	private final Flyway flyway;

	@Inject
	DBMigrator(DataSource dataSource) {
		this.flyway = initFlyway(dataSource);
	}

	private Flyway initFlyway(DataSource dataSource) {
		Flyway flyway = new Flyway();
		flyway.setSchemas(SCHEMA);
		flyway.setDataSource(dataSource);
		flyway.setLocations(LOCATION);
		return flyway;
	}

	public void setLocations(String... locations) {
		String[] newLocations = Arrays.copyOf(locations, locations.length + 1);
		newLocations[locations.length] = LOCATION;
		flyway.setLocations(newLocations);
	}

	public void migrate(Action action) throws SQLException {
		//Early return
		if (action == Action.NOP) {
			return;
		}
		switch (action) {
			case DROP_CREATE:
				flyway.clean();
			case UPDATE:
				flyway.migrate();
				break;
			default:
				throw new IllegalArgumentException("Unknown action " + action);
		}
	}
}

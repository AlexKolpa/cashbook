package com.github.alexkolpa.cashbook.db;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.github.alexkolpa.cashbook.endpoints.FlowResource;
import me.magnet.relations.entities.tables.records.FlowsRecord;
import org.jooq.exception.DataAccessException;
import org.junit.Rule;
import org.junit.Test;
import org.postgresql.util.PSQLException;

public class FlowsTest {

	private static final String NOT_NULL_VIOLATION = "23502";
	private static final String CATEGORY_ID_COLUMN = "category_id";

	@Rule
	public final WithDB withDB = new WithDB("test-migrations/flows");

	@Test
	public void listAllSize() {
		withDB.transaction(context -> {
			List<FlowsRecord> list = Flows.list(context, 20, 0, Collections.emptySet());
			assertThat(list, hasSize(3));
		});

	}

	@Test
	public void listAllOrder() {
		withDB.transaction(context -> {
			Flows.list(context, 20, 0, Collections.emptySet())
					.stream()
					.map(FlowsRecord::getDate)
					.reduce((localDate, localDate2) -> {
						assertThat(localDate.compareTo(localDate2), greaterThanOrEqualTo(0));
						return localDate2;
					});
		});
	}

	@Test
	public void listCategory() {
		withDB.transaction(context -> {
			List<FlowsRecord> list = Flows.list(context, 20, 0, Collections.singleton(1L));
			assertThat(list, hasSize(2));
		});
	}

	@Test
	public void create() {
		withDB.transaction(context -> {
			FlowsRecord record = new FlowsRecord();
			record.setDate(LocalDate.now());
			record.setCost(500L);
			record.setCategoryId(1L);
			FlowsRecord result = Flows.create(context, record);
			assertThat(result.getDate(), is(record.getDate()));
			assertThat(result.getCategoryId(), is(record.getCategoryId()));
			assertThat(result.getCost(), is(record.getCost()));
			assertThat(result.getId(), is(notNullValue()));
		});
	}

	@Test
	public void createBroken() {
		try {
			withDB.transaction(context -> {
				FlowsRecord record = new FlowsRecord();
				record.setDate(LocalDate.now());
				record.setCost(500L);
				Flows.create(context, record);
				fail("Should not have succeeded");
			});
		}
		catch (DataAccessException e) {
			assertCategoryIdViolation(e);
		}
	}

	@Test
	public void update() {
		withDB.transaction(context -> {
			FlowsRecord record = new FlowsRecord();
			record.setDescription("foo");
			Optional<FlowsRecord> update = Flows.update(context, 1L, record);
			assertThat(update.isPresent(), is(true));
			FlowsRecord result = update.get();
			assertThat(result.getDescription(), is("foo"));
			assertThat(result.getCategoryId(), is(1L));
			assertThat(result.getId(), is(1L));
		});
	}

	@Test
	public void updateNonExisting() {
		withDB.transaction(context -> {
			FlowsRecord record = new FlowsRecord();
			record.setDescription("bla");
			Optional<FlowsRecord> update = Flows.update(context, 4L, record);
			assertThat(update.isPresent(), is(false));
		});
	}

	@Test
	public void updateBroken() {
		try {
			withDB.transaction(context -> {
				FlowsRecord record = new FlowsRecord();
				record.setCategoryId(null);
				Flows.update(context, 1L, record);
				fail("Should not have succeeded");
			});
		}
		catch (DataAccessException e) {
			assertCategoryIdViolation(e);
		}
	}

	@Test
	public void delete() {
		withDB.transaction(context -> {
			Optional<FlowsRecord> delete = Flows.delete(context, 1L);
			assertThat(delete.isPresent(), is(true));
		});
	}

	@Test
	public void deleteNonExisting() {
		withDB.transaction(context -> {
			Optional<FlowsRecord> delete = Flows.delete(context, 4L);
			assertThat(delete.isPresent(), is(false));
		});
	}
	private void assertCategoryIdViolation(DataAccessException e) {
		assertThat(e.getCause(), instanceOf(PSQLException.class));
		PSQLException cause = (PSQLException) e.getCause();
		assertThat(cause.getServerErrorMessage().getSQLState(), is(NOT_NULL_VIOLATION));
		assertThat(cause.getServerErrorMessage().getColumn(), is(CATEGORY_ID_COLUMN));
	}
}
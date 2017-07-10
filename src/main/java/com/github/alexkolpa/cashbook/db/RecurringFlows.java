package com.github.alexkolpa.cashbook.db;

import static me.magnet.relations.entities.tables.RecurringFlows.RECURRING_FLOWS;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import me.magnet.relations.entities.enums.FlowInterval;
import me.magnet.relations.entities.tables.records.RecurringFlowsRecord;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

public class RecurringFlows {

	public static List<RecurringFlowsRecord> list(DSLContext context, int limit, int offset,
			Set<Long> categories, FlowInterval interval) {
		Set<FlowInterval> intervals = allowedIntervals(interval);

		return context.selectFrom(RECURRING_FLOWS)
				.where(RECURRING_FLOWS.INTERVAL.in(intervals))
				.and(RECURRING_FLOWS.CATEGORY_ID.in(categories))
				.orderBy(RECURRING_FLOWS.DATE.desc())
				.limit(limit)
				.offset(offset)
				.fetch();
	}

	private static Set<FlowInterval> allowedIntervals(FlowInterval max) {
		return Optional.ofNullable(max)
				.map(Enum::ordinal)
				.map(ord -> Arrays.stream(FlowInterval.values())
						.filter(interval -> interval.ordinal() <= ord)
						.collect(Collectors.toSet()))
				.orElse(Collections.emptySet());
	}

	public static RecurringFlowsRecord create(DSLContext context, RecurringFlowsRecord record) {
		return context.insertInto(RECURRING_FLOWS)
				.set(record)
				.returning()
				.fetchOne();
	}

	public static Optional<RecurringFlowsRecord> update(DSLContext context, long id,
			RecurringFlowsRecord record) {
		return context.update(RECURRING_FLOWS)
				.set(record)
				.where(RECURRING_FLOWS.ID.eq(id))
				.returning()
				.fetchOptional();
	}

	public static Optional<RecurringFlowsRecord> delete(DSLContext context, long id) {
		return context.deleteFrom(RECURRING_FLOWS)
				.where(RECURRING_FLOWS.ID.eq(id))
				.returning()
				.fetchOptional();
	}
}

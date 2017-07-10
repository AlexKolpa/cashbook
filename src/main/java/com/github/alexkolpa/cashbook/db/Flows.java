package com.github.alexkolpa.cashbook.db;

import static me.magnet.relations.entities.tables.Flows.FLOWS;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import me.magnet.relations.entities.tables.records.FlowsRecord;
import org.jooq.DSLContext;

public class Flows {
	public static List<FlowsRecord> list(DSLContext context, int limit, int offset,
			Set<Long> categories) {
		return context.selectFrom(FLOWS)
				.where(FLOWS.CATEGORY_ID.in(categories))
				.orderBy(FLOWS.DATE.desc())
				.limit(limit)
				.offset(offset)
				.fetch();
	}

	public static FlowsRecord create(DSLContext context, FlowsRecord record) {
		return context.insertInto(FLOWS).set(record).returning().fetchOne();
	}

	public static Optional<FlowsRecord> update(DSLContext context, long id, FlowsRecord record) {
		return context.update(FLOWS)
				.set(record)
				.where(FLOWS.ID.eq(id))
				.returning()
				.fetchOptional();
	}

	public static Optional<FlowsRecord> delete(DSLContext context, long id) {
		return context.deleteFrom(FLOWS)
				.where(FLOWS.ID.eq(id))
				.returning()
				.fetchOptional();
	}
}

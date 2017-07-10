package com.github.alexkolpa.cashbook.db;

import static me.magnet.relations.entities.tables.SubCategories.SUB_CATEGORIES;

import java.util.List;
import java.util.Optional;

import me.magnet.relations.entities.tables.records.SubCategoriesRecord;
import org.jooq.DSLContext;

public class SubCategories {
	public static List<SubCategoriesRecord> list(DSLContext context) {
		return context.selectFrom(SUB_CATEGORIES).fetch();
	}

	public static SubCategoriesRecord create(DSLContext context, SubCategoriesRecord record) {
		return context.insertInto(SUB_CATEGORIES)
				.set(record)
				.returning()
				.fetchOne();
	}

	public static Optional<SubCategoriesRecord> update(DSLContext context, long id,
			SubCategoriesRecord record) {
		return context.update(SUB_CATEGORIES)
				.set(record)
				.where(SUB_CATEGORIES.ID.eq(id))
				.returning()
				.fetchOptional();
	}

	public static Optional<SubCategoriesRecord> delete(DSLContext context, long id) {
		return context.deleteFrom(SUB_CATEGORIES)
				.where(SUB_CATEGORIES.ID.eq(id))
				.returning()
				.fetchOptional();
	}
}

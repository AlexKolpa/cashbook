package com.github.alexkolpa.cashbook.db;

import static me.magnet.relations.entities.tables.Categories.CATEGORIES;

import java.util.List;
import java.util.Optional;

import me.magnet.relations.entities.tables.records.CategoriesRecord;
import org.jooq.DSLContext;

public class Categories {
	public static List<CategoriesRecord> list(DSLContext context) {
		return context.selectFrom(CATEGORIES).fetch();
	}

	public static CategoriesRecord create(DSLContext context, CategoriesRecord record) {
		return context.insertInto(CATEGORIES)
				.set(record)
				.returning()
				.fetchOne();
	}

	public static Optional<CategoriesRecord> update(DSLContext context, long id,
			CategoriesRecord record) {
		return context.update(CATEGORIES)
				.set(record)
				.where(CATEGORIES.ID.eq(id))
				.returning()
				.fetchOptional();
	}

	public static Optional<CategoriesRecord> delete(DSLContext context, long id) {
		return context.deleteFrom(CATEGORIES)
				.where(CATEGORIES.ID.eq(id))
				.returning()
				.fetchOptional();
	}
}

package com.github.alexkolpa.cashbook.models.mappers;

import com.github.alexkolpa.cashbook.models.Category;
import me.magnet.relations.entities.tables.records.CategoriesRecord;

public class CategoryModels {
	public static Category toModel(CategoriesRecord record) {
		return new Category()
				.setId(record.getId())
				.setName(record.getName());
	}

	public static CategoriesRecord toRecord(Category model) {
		CategoriesRecord record = new CategoriesRecord();
		record.setName(model.getName());
		return record;
	}
}

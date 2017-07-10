package com.github.alexkolpa.cashbook.models.mappers;

import com.github.alexkolpa.cashbook.models.Entity;
import com.github.alexkolpa.cashbook.models.SubCategory;
import me.magnet.relations.entities.tables.records.SubCategoriesRecord;

public class SubCategoryModels {
	public static SubCategory toModel(SubCategoriesRecord record) {
		return new SubCategory()
				.setId(record.getId())
				.setCategory(new Entity(record.getCategoryId()))
				.setName(record.getName());
	}

	public static SubCategoriesRecord toRecord(SubCategory model) {
		SubCategoriesRecord record = new SubCategoriesRecord();
		record.setName(model.getName());
		return record;
	}
}

package com.github.alexkolpa.cashbook.models.mappers;

import java.util.Optional;

import com.github.alexkolpa.cashbook.models.Entity;
import com.github.alexkolpa.cashbook.models.RecurringFlow;
import me.magnet.relations.entities.tables.records.RecurringFlowsRecord;

public class RecurringFlowModels {
	public static RecurringFlow toModel(RecurringFlowsRecord record) {
		return new RecurringFlow()
				.setId(record.getId())
				.setCategory(new Entity(record.getCategoryId()))
				.setSubCategory(Optional.ofNullable(record.getSubCategoryId())
						.map(Entity::new)
						.orElse(null))
				.setDate(record.getDate())
				.setCost(record.getCost())
				.setDescription(record.getDescription())
				.setInterval(record.getInterval());
	}

	public static RecurringFlowsRecord toRecord(RecurringFlow flow) {
		RecurringFlowsRecord record = new RecurringFlowsRecord();
		record.setCategoryId(flow.getCategory().getId());
		Optional.ofNullable(flow.getSubCategory())
				.map(Entity::getId)
				.ifPresent(record::setSubCategoryId);
		record.setDescription(flow.getDescription());
		record.setDate(flow.getDate());
		record.setCost(flow.getCost());
		record.setInterval(flow.getInterval());
		return record;
	}
}

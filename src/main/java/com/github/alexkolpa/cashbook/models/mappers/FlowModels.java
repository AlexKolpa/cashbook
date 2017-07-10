package com.github.alexkolpa.cashbook.models.mappers;

import java.util.Optional;

import com.github.alexkolpa.cashbook.models.Entity;
import com.github.alexkolpa.cashbook.models.Flow;
import me.magnet.relations.entities.tables.records.FlowsRecord;

public class FlowModels {
	public static Flow toModel(FlowsRecord record) {
		return new Flow()
				.setId(record.getId())
				.setCategory(new Entity(record.getCategoryId()))
				.setSubCategory(Optional.ofNullable(record.getSubCategoryId())
						.map(Entity::new)
						.orElse(null))
				.setDate(record.getDate())
				.setCost(record.getCost())
				.setDescription(record.getDescription());
	}

	public static FlowsRecord toRecord(Flow flow) {
		FlowsRecord record = new FlowsRecord();
		record.setCategoryId(flow.getCategory().getId());
		Optional.ofNullable(flow.getSubCategory())
				.map(Entity::getId)
				.ifPresent(record::setSubCategoryId);
		record.setDescription(flow.getDescription());
		record.setDate(flow.getDate());
		record.setCost(flow.getCost());
		return record;
	}

}

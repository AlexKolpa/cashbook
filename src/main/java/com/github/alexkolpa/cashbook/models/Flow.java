package com.github.alexkolpa.cashbook.models;

import java.time.LocalDate;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Flow {
	private long id;
	private LocalDate date;
	private Entity category;
	private Entity subCategory;
	private String description;
	private long cost;
}

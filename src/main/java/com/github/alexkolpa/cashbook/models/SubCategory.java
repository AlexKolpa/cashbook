package com.github.alexkolpa.cashbook.models;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SubCategory {
	private long id;
	private Entity category;
	private String name;
}

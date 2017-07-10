package com.github.alexkolpa.cashbook.models;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Category {
	private long id;
	private String name;
}

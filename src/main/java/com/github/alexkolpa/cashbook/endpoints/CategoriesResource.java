package com.github.alexkolpa.cashbook.endpoints;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.util.List;
import java.util.stream.Collectors;

import com.github.alexkolpa.cashbook.db.Categories;
import com.github.alexkolpa.cashbook.db.ContextProvider;
import com.github.alexkolpa.cashbook.models.Category;
import com.github.alexkolpa.cashbook.models.mappers.CategoryModels;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import me.magnet.relations.entities.tables.records.CategoriesRecord;
import org.jooq.DSLContext;

@Path("categories")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE, onConstructor = @__(@Inject))
public class CategoriesResource {

	private final ContextProvider contextProvider;

	@GET
	public List<Category> listCategories() {
		return contextProvider.transactionResult(context -> Categories.list(context)
				.stream()
				.map(CategoryModels::toModel)
				.collect(Collectors.toList()));
	}

	@POST
	public Category create(Category category) {
		return contextProvider.transactionResult(context -> {
			CategoriesRecord record = CategoryModels.toRecord(category);
			return CategoryModels.toModel(Categories.create(context, record));
		});
	}

	@PUT
	@Path("{id:\\d+}")
	public Category update(@PathParam("id") long id, Category category) {
		return contextProvider.transactionResult(
				context -> Categories.update(context, id, CategoryModels.toRecord(category))
						.map(CategoryModels::toModel)
						.orElseThrow(NotFoundException::new));
	}

	@DELETE
	@Path("{id:\\d+}")
	public void delete(@PathParam("id") long id) {
		contextProvider.transaction(
				context -> Categories.delete(context, id).orElseThrow(NotFoundException::new));
	}
}

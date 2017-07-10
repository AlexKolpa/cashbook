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
import javax.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

import com.github.alexkolpa.cashbook.db.SubCategories;
import com.github.alexkolpa.cashbook.models.Category;
import com.github.alexkolpa.cashbook.models.SubCategory;
import com.github.alexkolpa.cashbook.models.mappers.SubCategoryModels;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import me.magnet.relations.entities.tables.records.SubCategoriesRecord;
import org.jboss.resteasy.plugins.guice.RequestScoped;
import org.jooq.DSLContext;

@Path("sub-categories")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE, onConstructor = @__(@Inject))
public class SubCategoriesResource {

	private final Provider<DSLContext> contextProvider;

	@GET
	public List<SubCategory> listSubCategories() {
		try (DSLContext context = contextProvider.get()) {
			return SubCategories.list(context)
					.stream()
					.map(SubCategoryModels::toModel)
					.collect(Collectors.toList());
		}
	}

	@POST
	public SubCategory create(SubCategory subCategory) {
		try (DSLContext context = contextProvider.get()) {
			SubCategoriesRecord record = SubCategoryModels.toRecord(subCategory);
			return SubCategoryModels.toModel(SubCategories.create(context, record));
		}
	}

	@PUT
	@Path("{id:\\d+}")
	public SubCategory update(@PathParam("id") long id, SubCategory subCategory) {
		try (DSLContext context = contextProvider.get()) {
			return SubCategories.update(context, id, SubCategoryModels.toRecord(subCategory))
					.map(SubCategoryModels::toModel)
					.orElseThrow(NotFoundException::new);
		}
	}

	@DELETE
	@Path("{id:\\d+}")
	public void delete(@PathParam("id") long id) {
		try (DSLContext context = contextProvider.get()) {
			SubCategories.delete(context, id).orElseThrow(NotFoundException::new);
		}
	}
}

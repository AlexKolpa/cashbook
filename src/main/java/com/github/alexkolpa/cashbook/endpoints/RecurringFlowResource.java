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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.alexkolpa.cashbook.db.Flows;
import com.github.alexkolpa.cashbook.db.RecurringFlows;
import com.github.alexkolpa.cashbook.models.Flow;
import com.github.alexkolpa.cashbook.models.RecurringFlow;
import com.github.alexkolpa.cashbook.models.mappers.FlowModels;
import com.github.alexkolpa.cashbook.models.mappers.RecurringFlowModels;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import me.magnet.relations.entities.enums.FlowInterval;
import me.magnet.relations.entities.tables.records.FlowsRecord;
import me.magnet.relations.entities.tables.records.RecurringFlowsRecord;
import org.jboss.resteasy.plugins.guice.RequestScoped;
import org.jooq.DSLContext;

@Path("recurring-flows")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE, onConstructor = @__(@Inject))
public class RecurringFlowResource {

	private final Provider<DSLContext> contextProvider;

	@GET
	public List<RecurringFlow> getRecurringFlows(@QueryParam("limit") int limit,
			@QueryParam("offset") int offset, @QueryParam("category") Set<Long> categories,
			@QueryParam("max-interval") FlowInterval interval) {
		try (DSLContext context = contextProvider.get()) {
			return RecurringFlows.list(context, limit, offset, categories, interval)
					.stream()
					.map(RecurringFlowModels::toModel)
					.collect(Collectors.toList());
		}
	}

	@POST
	public RecurringFlow createRecurringFlow(RecurringFlow recurringFlow) {
		try (DSLContext context = contextProvider.get()) {
			RecurringFlowsRecord record = RecurringFlowModels.toRecord(recurringFlow);
			return RecurringFlowModels.toModel(RecurringFlows.create(context, record));
		}
	}

	@PUT
	@Path("{id:\\d+}")
	public RecurringFlow updateRecurringFlow(@PathParam("id") long id,
			RecurringFlow recurringFlow) {
		try (DSLContext context = contextProvider.get()) {
			return RecurringFlows.update(context, id, RecurringFlowModels.toRecord(recurringFlow))
					.map(RecurringFlowModels::toModel)
					.orElseThrow(NotFoundException::new);
		}
	}

	@DELETE
	@Path("{id:\\d+}")
	public void delete(@PathParam("id") long id) {
		try (DSLContext context = contextProvider.get()) {
			RecurringFlows.delete(context, id).orElseThrow(NotFoundException::new);
		}
	}
}

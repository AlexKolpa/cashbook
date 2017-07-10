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
import com.github.alexkolpa.cashbook.models.Flow;
import com.github.alexkolpa.cashbook.models.mappers.FlowModels;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import me.magnet.relations.entities.tables.records.FlowsRecord;
import org.jooq.DSLContext;

@Path("flows")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE, onConstructor = @__(@Inject))
public class FlowResource {

	private final Provider<DSLContext> contextProvider;

	@GET
	public List<Flow> getFlows(@QueryParam("limit") int limit, @QueryParam("offset") int offset,
			@QueryParam("category") Set<Long> categories) {
		try (DSLContext context = contextProvider.get()) {
			return Flows.list(context, limit, offset, categories)
					.stream()
					.map(FlowModels::toModel)
					.collect(Collectors.toList());
		}
	}

	@POST
	public Flow createFlow(Flow flow) {
		try (DSLContext context = contextProvider.get()) {
			FlowsRecord record = FlowModels.toRecord(flow);
			return FlowModels.toModel(Flows.create(context, record));
		}
	}

	@PUT
	@Path("{id:\\d+}")
	public Flow updateFlow(@PathParam("id") long id, Flow flow) {
		try (DSLContext context = contextProvider.get()) {
			return Flows.update(context, id, FlowModels.toRecord(flow))
					.map(FlowModels::toModel)
					.orElseThrow(NotFoundException::new);
		}
	}

	@DELETE
	@Path("{id:\\d+}")
	public void delete(@PathParam("id") long id) {
		try (DSLContext context = contextProvider.get()) {
			Flows.delete(context, id).orElseThrow(NotFoundException::new);
		}
	}
}

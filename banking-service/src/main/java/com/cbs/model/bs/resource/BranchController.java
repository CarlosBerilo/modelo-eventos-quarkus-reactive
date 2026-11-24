package com.cbs.model.bs.resource;

import com.cbs.model.bs.domain.Branch;
import com.cbs.model.bs.service.BranchService;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/branchs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BranchController {

    @Inject
    BranchService branchService;

    @POST
    public Uni<Response> create(@Valid Branch branch) {
        Log.info("Processing request in branchController create"); 
        return branchService.create(branch)
                                                .map(branchCreate -> 
                                                    Response.status(Response.Status.CREATED)
                                                    .entity(branchCreate)
                                                    .build()
                                                );
    }

    @PUT
    @Path("/{id}")
    public Uni<Response> update(@PathParam("id") Long id, @Valid Branch branch) {
        Log.info("Processing request in branchController update");
        return branchService.update(id, branch)
                                                .map(branchUpdate -> 
                                                    Response.ok(branchUpdate)
                                                    .build()
                                                );
    }
}


package com.cbs.model.bs.service;

import org.eclipse.microprofile.reactive.messaging.Channel;

import com.cbs.model.bs.domain.Branch;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.MutinyEmitter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class BranchCreatedProducer {

    @Inject
    @Channel("branch-events-created-out")
    MutinyEmitter<Branch> branchEmitter;

          /**
           * Creates a new branch event and sends it to Kafka.
           * Emits to Kafka reactively and returns the sent object.
           *
           * @param branchCreate the new branch
           * @return {@code Uni<Branch>} a Uni emitting the created branch
           */
    public Uni<Branch> create(Branch branchCreate) {
        Log.info("Processing 'create' publication to Kafka [CREATED] " );
        return branchEmitter.send(branchCreate)
                .replaceWith(branchCreate);
    }
}
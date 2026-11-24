package com.cbs.model.bs.service;


import org.eclipse.microprofile.reactive.messaging.Channel;

import com.cbs.model.bs.domain.Branch;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.MutinyEmitter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class BranchUpdateProducer {

    @Inject
    @Channel("branch-events-updated-out")
    MutinyEmitter<Branch> branchUpdateEmitter;

    public Uni<Branch> update(Branch updateBranch) {
        Log.info("Processing 'update ' publication to Kafka  [UPDATED] " );
        return branchUpdateEmitter.send(updateBranch)
                .replaceWith(updateBranch);
    }
}


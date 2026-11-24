package com.cbs.model.bs.service;


import org.eclipse.microprofile.reactive.messaging.Channel;

import com.cbs.model.bs.domain.Branch;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.MutinyEmitter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class BranchCreateProducer {

    @Inject
    @Channel("branch-events-pending-out")
    MutinyEmitter<Branch> branchCreateEmitter;
    
    public Uni<Branch> create(Branch newBranch) {
         Log.info("Processing 'create' publication to Kafka  [PENDING] " );
         return branchCreateEmitter.send(newBranch)
                .replaceWith(newBranch);
                
    }

}


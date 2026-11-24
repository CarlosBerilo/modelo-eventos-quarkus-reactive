package com.cbs.model.bs.service;


import org.eclipse.microprofile.reactive.messaging.Channel;

import com.cbs.model.bs.domain.Branch;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.MutinyEmitter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class BranchAuditedProducer {

    @Inject
    @Channel("branch-events-audited-out")
    MutinyEmitter<Branch> branchAuditedEmitter;
    
    public Uni<Branch> audited(Branch branch, String topico) {
        Log.info("Processing post to topic : " + topico + " [  " + branch + " ]");
        return branchAuditedEmitter.send(branch)
                .replaceWith(branch)
                .onItem().invoke( () -> Log.info(" \n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n "))
                .onItem().invoke(sentBranch -> Log.info("Audited message published to Kafka : " + sentBranch))
                .onFailure().invoke(error -> Log.error("Failed to post audited message to Kafka ", error));
    }

}

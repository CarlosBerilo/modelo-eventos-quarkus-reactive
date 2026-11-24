package com.cbs.model.bs.service;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import com.cbs.model.bs.domain.Branch;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class BranchCreatedConsumer {

          @Inject
          BranchAuditedProducer branchAuditedProducer;

          @Incoming ("banking-branch-created")
          public Uni<Void> receiveMessageBranch(Branch branch){
                    Log.info("Processando audited do topico banking-branch-created : [ " + branch.toString() + " ]");                    
                    return branchAuditedProducer.audited(branch, "banking-branch-created").replaceWithVoid();
          }

}



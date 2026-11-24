package com.cbs.model.bs.service;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import com.cbs.model.bs.domain.Branch;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class BranchPendingConsumer {

          @Inject
          BranchAuditedProducer branchAuditedProducer;
          
          @Incoming ("banking-branch-pending")
          public Uni<Void> receiveMessageBranch(Branch branch){
                    Log.info("Processing audited topic banking-branch-pending  :  [ " + branch.toString() + " ] ");
                    return branchAuditedProducer.audited(branch, "banking-branch-pending").replaceWithVoid();
          }

}

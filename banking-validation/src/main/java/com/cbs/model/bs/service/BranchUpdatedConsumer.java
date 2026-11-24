package com.cbs.model.bs.service;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import com.cbs.model.bs.domain.Branch;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class BranchUpdatedConsumer {

          @Inject 
          BranchService  branchService ;

          /**
           * Receives branch update events from Kafka and processes the update.
           *
           * @param branch the branch entity received from the update event
           * @return {@code Uni<Branch>} a Uni emitting the updated branch
           */
          @Incoming ("banking-branch-updated")
          public void  receiveMessageBranch(Branch branch){                   
                   branchService.update(branch);
          }

}

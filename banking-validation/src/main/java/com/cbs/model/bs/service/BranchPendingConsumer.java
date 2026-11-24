package com.cbs.model.bs.service;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import com.cbs.model.bs.domain.Branch;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Consumidor de eventos para agências
 * BranchConsumer
 */
@ApplicationScoped
public class BranchPendingConsumer {

          @Inject 
          BranchService  branchService ;

          /**
           * Receives branch creation events from Kafka and processes the creation.
           *
           * @param branch the branch entity received from the creation event
           * @return {@code Uni<Branch>} a Uni emitting the created branch
           */
          @Incoming ("banking-branch-pending")
          public Uni<Branch>  receiveMessageBranch(Branch branch){                   
                   return branchService.create(branch);
          }

}

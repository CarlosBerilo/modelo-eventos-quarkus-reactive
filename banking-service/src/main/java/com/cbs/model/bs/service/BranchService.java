package com.cbs.model.bs.service;

import java.time.Instant;

import com.cbs.model.bs.domain.Branch;
import com.cbs.model.bs.domain.BranchStatus;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Service for branch management.
 */

@ApplicationScoped
public class BranchService {

    @Inject
    BranchCreateProducer branchCreateProducer;

    @Inject
    BranchUpdateProducer branchUpdateProducer;

    /**
     * Creates a new branch activated event and sends it to Kafka.
     * Emits to Kafka reactively and returns the sent object.
     *
     * @param branch the branch entity to be published
     * @return {@code Uni<Branch>} a Uni emitting the created branch
     */

    public Uni<Branch> create(Branch branch) {
        Log.info("Processando requisição em branchService  create  :  " + branch.toString());
                Branch newbranch = new Branch(
                null,
                branch.name(),
                branch.legalName(),
                branch.cnpj(),
                BranchStatus.PENDING,
                Instant.now()
        );
        return branchCreateProducer.create(newbranch);
    }

    /**
     * Updates a branch event and sends it to Kafka.
     * Emits the update event to Kafka.
     *
     * @param id the identifier of the branch to update
     * @param updatedBranch the updated branch data
     * @return {@code Uni<Branch>} a Uni emitting the updated branch
     */

    public Uni<Branch> update(Long id, Branch branchAlterada) {
        Log.info("Processando requisição em branchService  update  " + branchAlterada.toString());
        Branch branchUpdate = new Branch(
                id,
                branchAlterada.name(),
                branchAlterada.legalName(),
                branchAlterada.cnpj(),
                branchAlterada.status(),
                Instant.now()
        );
        return branchUpdateProducer.update(branchUpdate);
    }

}


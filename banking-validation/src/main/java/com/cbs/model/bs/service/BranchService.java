package com.cbs.model.bs.service;

import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.cbs.model.bs.domain.Branch;
import com.cbs.model.bs.domain.BranchStatus;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@ApplicationScoped
public class BranchService {

    @Inject 
    BranchCreatedProducer branchCreatedProducer;

    @Inject
    Validator validator;

    private final AtomicLong idCounter = new AtomicLong(1000L);

    /**
     * Creates a new branch activated event and sends it to Kafka.
     * Emits to Kafka reactively and returns the sent object.
     *
     * @param branch the branch entity to be published
     * @return {@code Uni<Branch>} a Uni emitting the created branch
     */
    public Uni<Branch> create(Branch branch) {
        return this.validateCriticalData(branch)
                .onItem().transformToUni(branchValidada -> {                
                    Long novoId = idCounter.incrementAndGet();  
                    branchValidada.setId(novoId);
                    branchValidada.setStatus(BranchStatus.ACTIVE);
                    
                    Log.info("Publishing a validated agency, created on the Kafka bus." +  branchValidada.toString());
                    return branchCreatedProducer.create(branchValidada)
                                .replaceWith(branchValidada);
            });                
    }


        /**
         * Updates a branch event and sends it to Kafka.
         * Emits the update event to Kafka.
         *
         * @param branch the updated branch
         * @return {@code Uni<Branch>} a Uni emitting the updated branch
         */
    public void update(Branch branch) {
        Branch  branchValidated = (Branch) this.validateCriticalData(branch);
        Log.info("Publishing an updated and validated agency to the Kafka bus." +  branchValidated.toString());                   
                
    }


    /**
     * Validates critical branch fields (name, legalName, cnpj) using Bean Validation.
     *
     * @param branch the branch to be validated
     * @return {@code Uni<Branch>} a Uni emitting the validated branch or failing with an error message
     */
    public Uni<Branch> validateCriticalData(Branch branch) {
            Log.info("Validating critical registration data for the agency...");
            
            Set<ConstraintViolation<Branch>> violacoes = Stream.of("name", "legalName", "cnpj")
                    .flatMap(property -> validator.validateProperty(branch, property).stream())
                    .collect(Collectors.toSet());

            if (!violacoes.isEmpty()) {
                    String erros = violacoes.stream()
                            .map(ConstraintViolation::getMessage)
                            .collect(Collectors.joining(" | "));

                    return Uni.createFrom().failure(new IllegalArgumentException("Critical registration inconsistency: " + erros));
            }

            Log.info("Fields (name, legalName, cnpj) successfully approved.");
            return Uni.createFrom().item(branch);
    }


}


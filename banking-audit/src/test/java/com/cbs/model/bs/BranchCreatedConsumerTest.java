package com.cbs.model.bs;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.cbs.model.bs.domain.Branch;
import com.cbs.model.bs.service.BranchAuditedProducer;
import com.cbs.model.bs.service.BranchCreatedConsumer;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import jakarta.inject.Inject;

@QuarkusTest
class BranchCreatedConsumerTest {

    @InjectMock
    BranchAuditedProducer branchAuditedProducer;

    @Inject
    BranchCreatedConsumer consumer;

    private Branch branch;

    @BeforeEach
    void setUp() {
        branch = new Branch(1L, "Agencia Centro", "Banco Exemplo S.A.", "12345678000199", null);
    }

    @Test
    void shouldConsumeBranchCreatedAndForwardToAuditedProducer() {
        when(branchAuditedProducer.audited(branch, "banking-branch-created"))
                .thenReturn(Uni.createFrom().item(branch));

        consumer.receiveMessageBranch(branch)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();

        verify(branchAuditedProducer, times(1)).audited(branch, "banking-branch-created");
    }

    @Test
    void shouldFailWhenAuditedProducerFails() {
        when(branchAuditedProducer.audited(branch, "banking-branch-created"))
                .thenReturn(Uni.createFrom().failure(new RuntimeException("Producer failure")));

        consumer.receiveMessageBranch(branch)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertFailedWith(RuntimeException.class, "Producer failure");

        verify(branchAuditedProducer, times(1)).audited(branch, "banking-branch-created");
    }
}

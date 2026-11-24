package com.cbs.model.bs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.cbs.model.bs.domain.Branch;
import com.cbs.model.bs.service.BranchAuditedProducer;

import io.quarkus.arc.ClientProxy;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import io.smallrye.reactive.messaging.MutinyEmitter;
import jakarta.inject.Inject;

@QuarkusTest
class BranchAuditedProducerTest {

    @Inject
    BranchAuditedProducer producer;

    MutinyEmitter<Branch> branchAuditedEmitter;

    private Branch branch;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() throws Exception {
        branchAuditedEmitter = mock(MutinyEmitter.class);
        BranchAuditedProducer target = ClientProxy.unwrap(producer);
        Field emitterField = BranchAuditedProducer.class.getDeclaredField("branchAuditedEmitter");
        emitterField.setAccessible(true);
        emitterField.set(target, branchAuditedEmitter);

        branch = new Branch(1L, "Agencia Centro", "Banco Exemplo S.A.", "12345678000199", null);
    }

    @Test
    void shouldPublishAuditedMessageSuccessfully() {
        String topico = "banking-branch-created";
        when(branchAuditedEmitter.send(branch)).thenReturn(Uni.createFrom().voidItem());

        Branch result = producer.audited(branch, topico)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted()
                .getItem();

        assertEquals(branch, result);
        verify(branchAuditedEmitter, times(1)).send(branch);
    }

    @Test
    void shouldPropagateErrorWhenEmitterFails() {
        String topico = "banking-branch-created";
        RuntimeException expectedException = new RuntimeException("Kafka connection error");
        when(branchAuditedEmitter.send(branch)).thenReturn(Uni.createFrom().failure(expectedException));

        producer.audited(branch, topico)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertFailedWith(RuntimeException.class, "Kafka connection error");

        verify(branchAuditedEmitter, times(1)).send(branch);
    }
}

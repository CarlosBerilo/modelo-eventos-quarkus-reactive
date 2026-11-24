package com.cbs.model.bs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.cbs.model.bs.domain.Branch;
import com.cbs.model.bs.domain.BranchStatus;
import com.cbs.model.bs.service.BranchCreatedProducer;
import com.cbs.model.bs.service.BranchService;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import jakarta.inject.Inject;


@QuarkusTest
public class BranchServiceTest {


    @Inject
    BranchService branchService;

    @InjectMock
    BranchCreatedProducer branchCreatedProducer;

    private Branch branchInput;

    @BeforeEach
    void setUp() {
        branchInput = new Branch();
        branchInput.setName("Agência Centro"); 
        branchInput.setLegalName("Razão Social Agência Centro LTDA"); // Preenche a Razão Social exigida
        branchInput.setCnpj("76803621000175"); 
    }


    @Test
    @DisplayName("Deve criar uma agência com sucesso, gerar ID, definir status ACTIVE e publicar no Kafka")
    void deveCriarBranchComSucesso() {
    

        // Se o método validateCriticalData chama o validador e retorna Uni, simulamos o comportamento de sucesso do Kafka:
        when(branchCreatedProducer.create(any(Branch.class))).thenReturn(Uni.createFrom().item(branchInput));

        // Act
        Uni<Branch> resultUni = branchService.create(branchInput);

        // Assert usando o UniAssertSubscriber do Mutiny
        Branch branchResultado = resultUni.subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted()
                .getItem();

        assertNotNull(branchResultado);
        assertNotNull(branchResultado.getId(), "O ID deveria ter sido gerado");
        assertEquals(BranchStatus.ACTIVE, branchResultado.getStatus());
        
        // Verifica se o produtor do Kafka foi acionado exatamente uma vez com a branch modificada
        verify(branchCreatedProducer, times(1)).create(branchResultado);
    }

    
         
}

package com.cbs.model.bs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;  // Ajustado para o Enum padrão planejado
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;

import com.cbs.model.bs.domain.Branch;
import com.cbs.model.bs.domain.BranchStatus;
import com.cbs.model.bs.service.BranchCreateProducer;
import com.cbs.model.bs.service.BranchService;
import com.cbs.model.bs.service.BranchUpdateProducer;

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
    BranchCreateProducer branchCreateProducer;

    @InjectMock
    BranchUpdateProducer branchUpdateProducer;

    private Branch branchInputMock;

    @BeforeEach
    void setUp() {
        // Inicializa com ID null para simular uma requisição real de criação (POST)
        branchInputMock = new Branch(null, "Agência Centro", "Banco do Povo Centro S.A.", "10000000000100", BranchStatus.PENDING);
    }

@Test
void deveCriarBranchComSucesso() {
    // 1. Removemos a dependência de um ID fixo "1001" no mock de retorno do Kafka, 
    // fazendo ele apenas espelhar o ID que o Service decidir gerar.
    Mockito.when(branchCreateProducer.create(any(Branch.class)))
            .thenAnswer(invocation -> {
                Branch branchEnviada = invocation.getArgument(0);
                return Uni.createFrom().item(branchEnviada); // Retorna a própria branch com o ID dinâmico gerado
            });

    // Executa o pipeline reativo
    UniAssertSubscriber<Branch> subscriber = branchService.create(branchInputMock)
            .subscribe().withSubscriber(UniAssertSubscriber.create());

    // Captura o objeto final retornado pelo fluxo reativo
    subscriber.awaitItem();
    Branch branchRetornada = subscriber.getItem();


    // 2. Captura o argumento enviado ao produtor Kafka para garantir o casamento de dados
    ArgumentCaptor<Branch> branchCaptor = ArgumentCaptor.forClass(Branch.class);
    Mockito.verify(branchCreateProducer, Mockito.times(1)).create(branchCaptor.capture());

    Branch branchEnviadaAoKafka = branchCaptor.getValue();
    
    // Garante que o ID que passou pelo service foi exatamente o mesmo enviado ao Kafka
    assertEquals(branchRetornada.id(), branchEnviadaAoKafka.id(), "O ID enviado ao Kafka deve ser igual ao retornado");
}


    @Test
    void deveAtualizarBranchComSucesso() {
        Long idAlvo = 10L;
        // Na atualização (PUT), a agência já possui o ID alvo definido
        Branch branchAlteradaMock = new Branch(idAlvo, "Agência Centro Alterada", "Banco do Povo Centro S.A.", "10000000000100", BranchStatus.ACTIVE);

        Mockito.when(branchUpdateProducer.update(any(Branch.class)))
                .thenReturn(Uni.createFrom().item(branchAlteradaMock));

        // Como o método update foi planejado para receber o idAlvo e o corpo, passamos ambos
        UniAssertSubscriber<Branch> subscriber = branchService.update(idAlvo, branchAlteradaMock)
                .subscribe().withSubscriber(UniAssertSubscriber.create());

        subscriber.awaitItem()
                  .assertItem(branchAlteradaMock);

        Mockito.verify(branchUpdateProducer, Mockito.times(1)).update(any(Branch.class));
    }
}

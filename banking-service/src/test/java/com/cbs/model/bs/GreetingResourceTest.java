package com.cbs.model.bs;

import static org.hamcrest.CoreMatchers.is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mockito; // Ajuste para o pacote real da sua classe

import com.cbs.model.bs.domain.Branch;
import com.cbs.model.bs.domain.BranchStatus;
import com.cbs.model.bs.service.BranchService;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.smallrye.mutiny.Uni;

@QuarkusTest
class GreetingResourceTest {

    @InjectMock
    BranchService branchService;

    private Branch branchRequest;
    private Branch branchResponse;

    @BeforeEach
    void setUp() {
        // Objeto enviado pelo cliente (JSON de entrada)
        branchRequest = new Branch(null, "Agência Centro", "Banco do Povo Centro S.A.", "10000000000100", BranchStatus.ACTIVE);
        
        // Objeto retornado pelo Service (com ID já processado)
        branchResponse = new Branch(1L, "Agência Centro", "Banco do Povo Centro S.A.", "10000000000100", BranchStatus.ACTIVE);
    }

    @Test
    void deveRetornarStatus201AoCriarbranchComSucesso() {
        // Configura o mock do Service para retornar um Uni reativo de sucesso
        Mockito.when(branchService.create(any(Branch.class)))
                .thenReturn(Uni.createFrom().item(branchResponse));

        given()
            .contentType(ContentType.JSON)
            .body(branchRequest)
        .when()
            .post("/branchs")
        .then()
            .statusCode(201) // Valida status Response.Status.CREATED
            .contentType(ContentType.JSON)
            .body("id", is(1))
            .body("name", is("Agência Centro"))
            .body("status", is("ACTIVE"));

        Mockito.verify(branchService, Mockito.times(1)).create(any(Branch.class));
    }

        @Test
    void deveRetornarStatus200AoAtualizarbranchComSucesso() {
        Long idAlvo = 1L;
        
        // Configura o mock do Service para o método update
        Mockito.when(branchService.update(eq(idAlvo), any(Branch.class)))
                .thenReturn(Uni.createFrom().item(branchResponse));

        given()
            .pathParam("id", idAlvo)
            .contentType(ContentType.JSON)
            .body(branchRequest)
        .when()
            .put("/branchs/{id}")
        .then()
            .statusCode(200) // Valida status Response.ok()
            .contentType(ContentType.JSON)
            .body("id", is(1))
            .body("legalName", is("Banco do Povo Centro S.A."));

        Mockito.verify(branchService, Mockito.times(1)).update(eq(idAlvo), any(Branch.class));
    }
    
}
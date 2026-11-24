package com.cbs.model.bs.domain;


import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class BranchDeserializer extends ObjectMapperDeserializer<Branch> {
    public BranchDeserializer() {
        super(Branch.class);
    }
}

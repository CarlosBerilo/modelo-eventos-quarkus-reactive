package com.cbs.model.bs.domain;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class BranchJacksonDeserializer extends ObjectMapperDeserializer<Branch> {
    public BranchJacksonDeserializer() {
        super(Branch.class);
    }
}

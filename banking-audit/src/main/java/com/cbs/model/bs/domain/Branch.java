package com.cbs.model.bs.domain;


import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Branch (          
    Long id,

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name must be at most 100 characters")
    String name,

    @NotBlank(message = "Legal name cannot be blank")
    @Size(max = 150, message = "Legal name must be at most 150 characters")
    String legalName,

    @NotBlank(message = "CNPJ is required")
    @Pattern(regexp = "\\d{14}", message = "CNPJ must contain only 14 numeric digits")
    String cnpj,

    @NotNull(message = "Status is required")
    BranchStatus status,

     Instant occurredAt
) {

    public Branch(Long id, String name, String legalName, String cnpj, BranchStatus status) {
        this(id, name, legalName, cnpj, status, Instant.now()); 
    }

    public Branch(String name, String legalName, String cnpj, BranchStatus status) {
        this(null, name, legalName, cnpj, status, Instant.now());
    }
}

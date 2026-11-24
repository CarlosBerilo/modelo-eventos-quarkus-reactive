package com.cbs.model.bs.domain;


import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Branch {

    private Long id;

    @NotBlank(message = "O nome não pode estar em branco")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    private String name;

    @NotBlank(message = "A razão social não pode estar em branco")
    @Size(max = 150, message = "A razão social deve ter no máximo 150 caracteres")
    private String legalName;

    @NotBlank(message = "O CNPJ é obrigatório")
    @Pattern(regexp = "\\d{14}", message = "O CNPJ deve conter apenas os 14 dígitos numéricos")
    private String cnpj;

    @NotNull(message = "O status é obrigatório")
    private BranchStatus status;

    private Instant occurredAt;


    public Branch() {}

    public Branch(Long id, String name, String legalName, String cnpj, BranchStatus status) {
        this.id = id;
        this.name = name;
        this.legalName = legalName;
        this.cnpj = cnpj;
        this.status = status;
    }

    public Branch(String cnpj, Long id, String legalName, String name, Instant occurredAt, BranchStatus status) {
        this.cnpj = cnpj;
        this.id = id;
        this.legalName = legalName;
        this.name = name;
        this.occurredAt = occurredAt;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLegalName() { return legalName; }
    public void setLegalName(String legalName) { this.legalName = legalName; }
    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
    public BranchStatus getStatus() { return status; }
    public void setStatus(BranchStatus status) { this.status = status; }


    public Instant getOccurredAt() {
        return occurredAt;
    }

    public void setOccurredAt(Instant occurredAt) {
        this.occurredAt = occurredAt;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Branch{");
        sb.append("id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", legalName=").append(legalName);
        sb.append(", cnpj=").append(cnpj);
        sb.append(", status=").append(status);
        sb.append(", occurredAt=").append(occurredAt);
        sb.append('}');
        return sb.toString();
    }

    
}

package com.rewe.medical_record.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "diagnosis")
@NoArgsConstructor
@Getter
public class DiagnosisEntity {
    @Id
    private String name;
    @NotNull
    private String description;
}

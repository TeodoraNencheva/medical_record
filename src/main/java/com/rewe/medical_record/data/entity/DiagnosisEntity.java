package com.rewe.medical_record.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "diagnosis")
@NoArgsConstructor
@Getter
public class DiagnosisEntity extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String name;
    @NotNull
    private String description;
    @Column(columnDefinition = "boolean default false")
    private boolean deleted;
}

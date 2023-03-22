package com.rewe.medical_record.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "patient")
@NoArgsConstructor
public class PatientEntity extends BaseEntity {
    @NotBlank
    private String name;
    @ManyToOne
    private DoctorEntity gp;
    private boolean isInsured;
}

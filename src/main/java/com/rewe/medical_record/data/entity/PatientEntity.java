package com.rewe.medical_record.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "patient")
@NoArgsConstructor
public class PatientEntity extends BaseEntity {
    @NotNull
    @Size(min = 1)
    private String name;
    @ManyToOne
    private GeneralPractitionerEntity gp;
    private boolean isInsured;
}

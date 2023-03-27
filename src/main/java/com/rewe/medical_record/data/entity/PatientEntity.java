package com.rewe.medical_record.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "patient")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PatientEntity extends BaseEntity {
    @NotNull
    @Size(min = 2)
    private String name;
    @ManyToOne
    private GeneralPractitionerEntity gp;
    private boolean insured;
    @Column(columnDefinition = "boolean default false")
    private boolean deleted;
}

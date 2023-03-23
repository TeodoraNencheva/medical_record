package com.rewe.medical_record.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@NoArgsConstructor
public class GeneralPractitionerEntity extends DoctorEntity {
    @OneToMany(mappedBy = "gp")
    private Set<PatientEntity> patients;
}

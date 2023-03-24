package com.rewe.medical_record.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Set;

@Entity
@NoArgsConstructor
@Table(name = "general_practitioner")
public class GeneralPractitionerEntity extends DoctorEntity {
    @OneToMany(mappedBy = "gp")
    @JsonIgnore
    private Set<PatientEntity> patients;
    public Set<PatientEntity> getPatients() {
        return Collections.unmodifiableSet(patients);
    }
}
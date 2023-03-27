package com.rewe.medical_record.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

@Entity
@Table(name = "visit")
@NoArgsConstructor
@Getter
public class VisitEntity extends BaseEntity {
    @ManyToOne(optional = false)
    private PatientEntity patient;
    @ManyToOne(optional = false)
    private DoctorEntity doctor;
    @NotNull
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime dateTime;
    @NotNull
    @ManyToMany
    @JoinTable(name = "visits_diagnoses",
            joinColumns = @JoinColumn(name = "visit_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "diagnosis_id", referencedColumnName = "id"))
    private Set<DiagnosisEntity> diagnoses;
    @ManyToOne(optional = false)
    private FeeHistoryEntity fee;
    private boolean paidByPatient;
    public Set<DiagnosisEntity> getDiagnoses() {
        return Collections.unmodifiableSet(diagnoses);
    }
}

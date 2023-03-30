package com.rewe.medical_record.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "visit")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VisitEntity extends BaseEntity {
    @ManyToOne(optional = false)
    private PatientEntity patient;
    @ManyToOne(optional = false)
    private DoctorEntity doctor;
    @NotNull
    @Column(updatable = false)
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime dateTime = LocalDateTime.now();
    @NotNull
    @ManyToMany
    @JoinTable(name = "visits_diagnoses",
            joinColumns = @JoinColumn(name = "visit_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "diagnosis_id", referencedColumnName = "id"))
    private Set<DiagnosisEntity> diagnoses = new HashSet<>();
    @ManyToOne(optional = false)
    private FeeHistoryEntity fee;
    private boolean paidByPatient;
    @Column(columnDefinition = "boolean default false")
    private boolean deleted;
}

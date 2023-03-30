package com.rewe.medical_record.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "doctor")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
public class DoctorEntity extends BaseEntity {
    @NotNull
    @Size(min = 4)
    private String name;
    @NotNull
    @Past
    @Column(updatable = false)
    private LocalDate birthDate;
    @NotNull
    @ManyToMany
    @JoinTable(name = "doctors_specialties",
            joinColumns = @JoinColumn(name = "doctor_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "specialty_id", referencedColumnName = "id"))
    private Set<SpecialtyEntity> specialties = new HashSet<>();
    @Column(columnDefinition = "boolean default false")
    private boolean deleted;
}

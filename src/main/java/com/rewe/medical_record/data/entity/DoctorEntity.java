package com.rewe.medical_record.data.entity;

import com.rewe.medical_record.enums.Specialty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "doctor")
@NoArgsConstructor
@Getter
public class DoctorEntity extends BaseEntity {
    @NotBlank
    private String name;
    @NotNull
    @Past
    @Column(updatable = false)
    private LocalDate birthDate;
    @Enumerated(EnumType.STRING)
    @Column(updatable = false, nullable = false)
    private Specialty specialty;
    private boolean isGp;
}

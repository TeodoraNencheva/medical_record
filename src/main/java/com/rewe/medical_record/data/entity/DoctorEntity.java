package com.rewe.medical_record.data.entity;

import com.rewe.medical_record.enums.Specialty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "doctor")
@NoArgsConstructor
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
public class DoctorEntity extends BaseEntity {
    @NotBlank
    @Size(min = 4)
    private String name;
    @NotNull
    @Past
    @Column(updatable = false)
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate birthDate;
    @ElementCollection(targetClass = Specialty.class)
    @JoinTable(name = "doctors_specialties", joinColumns = @JoinColumn(name = "doctor_id"))
    @Column(name = "specialty", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Specialty> specialty;
}

package com.rewe.medical_record.data.dto.doctor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rewe.medical_record.validation.specialty.ValidSpecialties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddDoctorDto {
    @NotNull(message = "Name is required")
    @Size(min = 4, message = "Name should be 4 characters or more")
    private String name;
    @NotNull(message = "Birthdate is required")
    @Past(message = "Birthdate should be a past date")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate birthDate;
    @ValidSpecialties
    private Set<String> specialties;
}

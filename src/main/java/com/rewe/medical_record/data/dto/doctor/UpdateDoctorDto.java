package com.rewe.medical_record.data.dto.doctor;

import com.rewe.medical_record.validation.doctor.ExistingDoctorId;
import com.rewe.medical_record.validation.specialty.ValidSpecialties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDoctorDto {
    @ExistingDoctorId
    private Long id;
    @NotNull(message = "Name is required")
    @Size(min = 4, message = "Name should be 4 characters or more")
    private String name;
    @ValidSpecialties
    private Set<String> specialties;
}

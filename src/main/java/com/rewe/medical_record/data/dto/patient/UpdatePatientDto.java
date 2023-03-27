package com.rewe.medical_record.data.dto.patient;

import com.rewe.medical_record.validation.ExistingGpIdOrNull;
import com.rewe.medical_record.validation.ExistingPatientId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePatientDto {
    @ExistingPatientId
    private Long id;
    @NotNull(message = "Name is required")
    @Size(min = 2, message = "Name should be 2 or more characters")
    private String name;
    @ExistingGpIdOrNull
    private Long gpId;
    private boolean insured;
}

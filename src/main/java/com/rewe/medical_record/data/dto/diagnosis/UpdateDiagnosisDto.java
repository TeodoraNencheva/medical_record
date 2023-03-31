package com.rewe.medical_record.data.dto.diagnosis;

import com.rewe.medical_record.validation.diagnosis.ExistingDiagnosisId;
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
public class UpdateDiagnosisDto {
    @ExistingDiagnosisId
    private Long id;
    @NotNull(message = "Diagnosis name is required")
    @Size(min = 3, message = "Diagnosis name should be 3 or more characters")
    private String name;
    @NotNull(message = "Diagnosis description is required")
    @Size(min = 3, message = "Diagnosis description should be 3 or more characters")
    private String description;
}

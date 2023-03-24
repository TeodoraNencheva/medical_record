package com.rewe.medical_record.data.dto.patient;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
    @Positive
    @NotNull
    private Long id;
    @NotNull
    @Size(min = 2)
    private String name;
    @Positive
    private Long gpId;
    private boolean insured;
}

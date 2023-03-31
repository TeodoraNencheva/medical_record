package com.rewe.medical_record.data.dto.specialty;

import com.rewe.medical_record.validation.specialty.ExistingSpecialtyId;
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
public class UpdateSpecialtyDto {
    @ExistingSpecialtyId
    private Long id;
    @NotNull(message = "Specialty name is required")
    @Size(min = 3, message = "Specialty name should be 3 or more characters")
    private String name;
    private String description;
}

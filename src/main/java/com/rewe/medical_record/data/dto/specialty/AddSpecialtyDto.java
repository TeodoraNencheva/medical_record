package com.rewe.medical_record.data.dto.specialty;

import com.rewe.medical_record.validation.specialty.ValidNewSpecialtyName;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddSpecialtyDto {
    @ValidNewSpecialtyName
    @Size(min = 3, message = "Specialty name should be 3 or more characters")
    private String name;
    private String description;
}

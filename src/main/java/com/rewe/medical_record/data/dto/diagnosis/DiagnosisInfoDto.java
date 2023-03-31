package com.rewe.medical_record.data.dto.diagnosis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisInfoDto {
    private Long id;
    private String name;
    private String description;
}

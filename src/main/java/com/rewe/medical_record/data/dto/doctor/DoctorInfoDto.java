package com.rewe.medical_record.data.dto.doctor;

import com.rewe.medical_record.enums.Specialty;
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
public class DoctorInfoDto {
    private Long id;
    private String name;
    private LocalDate birthDate;
    private Set<Specialty> specialties;
}

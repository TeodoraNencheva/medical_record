package com.rewe.medical_record.data.dto.doctor;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate birthDate;
    private Set<String> specialties;
}

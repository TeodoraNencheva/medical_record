package com.rewe.medical_record.data.dto.visit;

import com.rewe.medical_record.validation.ExistingDoctorId;
import com.rewe.medical_record.validation.ExistingPatientId;
import com.rewe.medical_record.validation.ValidDiagnosesIds;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddVisitDto {
    @ExistingPatientId
    private Long patientId;
    @ExistingDoctorId
    private Long doctorId;
    @ValidDiagnosesIds
    private Set<Long> diagnoses;
}

package com.rewe.medical_record.data.dto.visit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VisitInfoDto {
    private Long id;
    private Long patientId;
    private Long doctorId;
    private LocalDateTime dateTime;
    private Set<String> diagnoses;
    private BigDecimal feeValue;
    private boolean paidByPatient;
}

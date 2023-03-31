package com.rewe.medical_record.validation.diagnosis;

import com.rewe.medical_record.data.repository.DiagnosisRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExistingDiagnosisIdValidator implements ConstraintValidator<ExistingDiagnosisId, Long> {
    private final DiagnosisRepository diagnosisRepository;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return diagnosisRepository.findByIdAndDeletedFalse(value).isPresent();
    }
}

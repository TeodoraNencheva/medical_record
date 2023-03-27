package com.rewe.medical_record.validation;

import com.rewe.medical_record.data.repository.PatientRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
public class ExistingPatientIdValidator implements ConstraintValidator<ExistingPatientId, Long> {
    private final PatientRepository patientRepository;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return Objects.nonNull(value) && patientRepository.findByIdAndDeletedFalse(value).isPresent();
    }
}

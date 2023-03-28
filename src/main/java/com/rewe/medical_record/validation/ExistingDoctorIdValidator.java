package com.rewe.medical_record.validation;

import com.rewe.medical_record.data.repository.DoctorRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
public class ExistingDoctorIdValidator implements ConstraintValidator<ExistingDoctorId, Long> {
    private final DoctorRepository doctorRepository;
    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return Objects.nonNull(value) && doctorRepository.findByIdAndDeletedFalse(value).isPresent();
    }
}

package com.rewe.medical_record.validation.specialty;

import com.rewe.medical_record.data.repository.SpecialtyRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
public class ExistingSpecialtyIdValidator implements ConstraintValidator<ExistingSpecialtyId, Long> {
    private final SpecialtyRepository specialtyRepository;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return Objects.nonNull(value) && specialtyRepository.findByIdAndDeletedFalse(value).isPresent();
    }
}

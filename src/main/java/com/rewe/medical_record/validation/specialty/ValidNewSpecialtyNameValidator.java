package com.rewe.medical_record.validation.specialty;

import com.rewe.medical_record.data.repository.SpecialtyRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
public class ValidNewSpecialtyNameValidator implements ConstraintValidator<ValidNewSpecialtyName, String> {
    private final SpecialtyRepository specialtyRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Objects.nonNull(value) && specialtyRepository.findByNameAndDeletedFalse(value).isEmpty();
    }
}

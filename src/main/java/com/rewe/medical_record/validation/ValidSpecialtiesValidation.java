package com.rewe.medical_record.validation;

import com.rewe.medical_record.data.repository.SpecialtyRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
public class ValidSpecialtiesValidation implements ConstraintValidator<ValidSpecialties, Set<String>> {
    private final SpecialtyRepository specialtyRepository;

    @Override
    public boolean isValid(Set<String> value, ConstraintValidatorContext context) {
        return Objects.nonNull(value) && (value.isEmpty() ||
                value.stream().noneMatch(n -> specialtyRepository.findByName(n).isEmpty()));
    }
}

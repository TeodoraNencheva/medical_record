package com.rewe.medical_record.validation;

import com.rewe.medical_record.data.repository.DiagnosisRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
public class ValidDiagnosesIdsValidator implements ConstraintValidator<ValidDiagnosesIds, Set<Long>> {
    private final DiagnosisRepository diagnosisRepository;

    @Override
    public boolean isValid(Set<Long> value, ConstraintValidatorContext context) {
        return Objects.nonNull(value) && !value.isEmpty() &&
                value.stream().allMatch(id -> diagnosisRepository.findById(id).isPresent());
    }
}

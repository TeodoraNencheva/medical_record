package com.rewe.medical_record.validation.visit;

import com.rewe.medical_record.data.repository.VisitRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExistingVisitIdValidator implements ConstraintValidator<ExistingVisitId, Long> {
    private final VisitRepository visitRepository;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return visitRepository.findByIdAndDeletedFalse(value).isPresent();
    }
}

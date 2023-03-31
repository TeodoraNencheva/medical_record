package com.rewe.medical_record.validation.doctor;

import com.rewe.medical_record.data.repository.GeneralPractitionerRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
public class ExistingGpIdOrNullValidator implements ConstraintValidator<ExistingGpIdOrNull, Long> {
    private final GeneralPractitionerRepository gpRepository;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return Objects.isNull(value) || gpRepository.findById(value).isPresent();
    }
}

package com.rewe.medical_record.validation;

import com.rewe.medical_record.data.repository.GeneralPractitionerRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class ExistingGpIdOrNullValidator implements ConstraintValidator<ExistingGpIdOrNull, Long> {
    private GeneralPractitionerRepository gpRepository;

    public ExistingGpIdOrNullValidator(GeneralPractitionerRepository gpRepository) {
        this.gpRepository = gpRepository;
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return Objects.isNull(value) || gpRepository.findById(value).isPresent();
    }
}

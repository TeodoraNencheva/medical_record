package com.rewe.medical_record.validation.diagnosis;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE, ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = ValidDiagnosesIdsValidator.class)
public @interface ValidDiagnosesIds {
    String message() default "Diagnoses IDs are required and must be valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

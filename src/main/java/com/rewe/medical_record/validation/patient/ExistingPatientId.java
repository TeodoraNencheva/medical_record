package com.rewe.medical_record.validation.patient;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE, ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = ExistingPatientIdValidator.class)
public @interface ExistingPatientId {
    String message() default "No patient with the given ID found";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

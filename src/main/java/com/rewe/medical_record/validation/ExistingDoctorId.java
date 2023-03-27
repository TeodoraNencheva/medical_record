package com.rewe.medical_record.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE, ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = ExistingDoctorIdValidator.class)
public @interface ExistingDoctorId {
    String message() default "No doctor with the given ID found";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

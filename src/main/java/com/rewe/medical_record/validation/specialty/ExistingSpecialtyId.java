package com.rewe.medical_record.validation.specialty;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE, ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = ExistingSpecialtyIdValidator.class)
public @interface ExistingSpecialtyId {
    String message() default "No specialty with the given ID found";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

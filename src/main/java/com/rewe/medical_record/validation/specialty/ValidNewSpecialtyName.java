package com.rewe.medical_record.validation.specialty;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE, ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = ValidNewSpecialtyNameValidator.class)
public @interface ValidNewSpecialtyName {
    String message() default "Specialty name should be unique";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

package com.rewe.medical_record.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE, ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = ExistingGpIdOrNullValidator.class)
public @interface ExistingGpIdOrNull {
    String message() default "No GP with the given ID found";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

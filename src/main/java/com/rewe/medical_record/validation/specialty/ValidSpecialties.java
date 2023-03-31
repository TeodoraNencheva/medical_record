package com.rewe.medical_record.validation.specialty;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE, ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = ValidSpecialtiesValidator.class)
public @interface ValidSpecialties {
    String message() default "Please provide valid specialty names or empty collection";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

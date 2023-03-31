package com.rewe.medical_record.validation.diagnosis;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE, ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = ValidNewDiagnosisNameValidator.class)
public @interface ValidNewDiagnosisName {
    String message() default "Diagnosis name should be unique";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

package com.rewe.medical_record.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@AllArgsConstructor
@Getter
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Patient not found")
public class PatientNotFoundException extends RuntimeException {
    private Long id;
}

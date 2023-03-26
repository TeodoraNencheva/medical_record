package com.rewe.medical_record.exceptions;

import lombok.Getter;

@Getter
public class PatientNotFoundException extends BaseException {
    private final String message = String.format("Patient with ID %s not found", this.getId());
    public PatientNotFoundException(Long id) {
        super(id);
        super.setMessage(message);
    }
}

package com.rewe.medical_record.exceptions;

import lombok.Getter;

@Getter
public class DiagnosisNotFoundException extends BaseException {
    private final String message = String.format("Diagnosis with ID %s not found", this.getId());

    public DiagnosisNotFoundException(Long id) {
        super(id);
        super.setMessage(message);
    }
}

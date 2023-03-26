package com.rewe.medical_record.exceptions;

import lombok.Getter;

@Getter
public class DoctorNotFoundException extends BaseException {
    private final String message = String.format("Doctor with ID %s not found", this.getId());
    public DoctorNotFoundException(Long id) {
        super(id);
        super.setMessage(message);
    }
}

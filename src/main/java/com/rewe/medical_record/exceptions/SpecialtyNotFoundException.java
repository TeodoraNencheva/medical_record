package com.rewe.medical_record.exceptions;

import lombok.Getter;

@Getter
public class SpecialtyNotFoundException extends BaseException {
    private final String message = String.format("Specialty with ID %s not found", this.getId());

    public SpecialtyNotFoundException(Long id) {
        super(id);
        super.setMessage(message);
    }
}

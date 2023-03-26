package com.rewe.medical_record.exceptions;

import lombok.Getter;

@Getter
public class GeneralPractitionerNotFoundException extends BaseException {
    private final String message = String.format("General practitioner with ID %s not found", this.getId());
    public GeneralPractitionerNotFoundException(Long id) {
        super(id);
        super.setMessage(message);
    }
}

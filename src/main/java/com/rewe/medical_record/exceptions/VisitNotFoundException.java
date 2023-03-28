package com.rewe.medical_record.exceptions;

import lombok.Getter;

@Getter
public class VisitNotFoundException extends BaseException {
    private final String message = String.format("Visit with ID %s not found", this.getId());

    public VisitNotFoundException(Long id) {
        super(id);
        super.setMessage(message);
    }
}

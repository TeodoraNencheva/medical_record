package com.rewe.medical_record.exceptions;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private Long id;
    private String message;

    public BaseException(Long id) {
        this.id = id;
    }

    protected BaseException setMessage(String message) {
        this.message = message;
        return this;
    }
}

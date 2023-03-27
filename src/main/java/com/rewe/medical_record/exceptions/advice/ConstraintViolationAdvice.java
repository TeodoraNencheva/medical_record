package com.rewe.medical_record.exceptions.advice;

import com.rewe.medical_record.service.ResponseService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class ConstraintViolationAdvice {
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> onConstraintViolation(ConstraintViolationException ex) {
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> constraintViolation : ex.getConstraintViolations()) {
            errors.add(constraintViolation.getMessage());
        }

        Map<String, Object> body = ResponseService.generateGeneralResponse(errors);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}

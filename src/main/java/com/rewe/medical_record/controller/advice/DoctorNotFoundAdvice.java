package com.rewe.medical_record.controller.advice;

import com.rewe.medical_record.exceptions.DoctorNotFoundException;
import com.rewe.medical_record.service.ResponseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ControllerAdvice
public class DoctorNotFoundAdvice {
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler({DoctorNotFoundException.class})
    public ResponseEntity<Object> onDoctorNotFound(DoctorNotFoundException ex) {
        Map<String, Object> body = ResponseService.generateGeneralResponse(
                String.format("Doctor with ID %s not found", ex.getId()));

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}

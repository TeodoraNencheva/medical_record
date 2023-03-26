package com.rewe.medical_record.controller.advice;

import com.rewe.medical_record.exceptions.GeneralPractitionerNotFoundException;
import com.rewe.medical_record.service.ResponseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ControllerAdvice
public class GeneralPractitionerNotFoundAdvice {
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler({GeneralPractitionerNotFoundException.class})
    public ResponseEntity<Object> onGPNotFound(GeneralPractitionerNotFoundException ex) {
        Map<String, Object> body = ResponseService.generateGeneralResponse(
                String.format("General practitioner with ID %s not found", ex.getId()));

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}

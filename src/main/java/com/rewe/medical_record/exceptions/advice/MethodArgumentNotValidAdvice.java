package com.rewe.medical_record.exceptions.advice;

import com.rewe.medical_record.service.ResponseService;
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
public class MethodArgumentNotValidAdvice {
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Object> onMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.add(fieldError.getDefaultMessage());
        }

        Map<String, Object> body = ResponseService.generateGeneralResponse(errors);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}

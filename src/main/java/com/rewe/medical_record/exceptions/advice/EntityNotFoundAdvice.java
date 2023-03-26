package com.rewe.medical_record.exceptions.advice;

import com.rewe.medical_record.exceptions.BaseException;
import com.rewe.medical_record.service.ResponseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ControllerAdvice
public class EntityNotFoundAdvice {
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler({BaseException.class})
    public ResponseEntity<Object> onEntityNotFound(BaseException ex) {
        Map<String, Object> body = ResponseService.generateGeneralResponse(ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}

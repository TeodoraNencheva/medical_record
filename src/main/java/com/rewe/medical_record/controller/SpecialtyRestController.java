package com.rewe.medical_record.controller;

import com.rewe.medical_record.data.dto.specialty.AddSpecialtyDto;
import com.rewe.medical_record.data.dto.specialty.SpecialtyInfoDto;
import com.rewe.medical_record.data.dto.specialty.UpdateSpecialtyDto;
import com.rewe.medical_record.service.ResponseService;
import com.rewe.medical_record.service.SpecialtyService;
import com.rewe.medical_record.validation.specialty.ExistingSpecialtyId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/specialties")
public class SpecialtyRestController {
    private final SpecialtyService specialtyService;

    @GetMapping
    public ResponseEntity<List<SpecialtyInfoDto>> getAllSpecialties() {
        return ResponseEntity.ok(specialtyService.getAllSpecialties());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpecialtyInfoDto> getSpecialty(@ExistingSpecialtyId @PathVariable Long id) {
        return ResponseEntity.ok(specialtyService.getSpecialty(id));
    }

    @PostMapping
    public ResponseEntity<SpecialtyInfoDto> createSpecialty(@Valid @RequestBody AddSpecialtyDto specialtyDto) {
        return new ResponseEntity<>(specialtyService.createSpecialty(specialtyDto), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<SpecialtyInfoDto> updateSpecialty(@Valid @RequestBody UpdateSpecialtyDto specialtyDto) {
        return ResponseEntity.ok(specialtyService.updateSpecialty(specialtyDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteSpecialty(@ExistingSpecialtyId @PathVariable Long id) {
        specialtyService.deleteSpecialty(id);
        Map<String, Object> response = ResponseService.generateGeneralResponse("Specialty with the given ID deleted");
        return ResponseEntity.ok(response);
    }
}

package com.rewe.medical_record.controller;

import com.rewe.medical_record.data.dto.diagnosis.AddDiagnosisDto;
import com.rewe.medical_record.data.dto.diagnosis.DiagnosisInfoDto;
import com.rewe.medical_record.data.dto.diagnosis.UpdateDiagnosisDto;
import com.rewe.medical_record.service.DiagnosisService;
import com.rewe.medical_record.service.ResponseService;
import com.rewe.medical_record.validation.diagnosis.ExistingDiagnosisId;
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
@RequestMapping("/diagnoses")
public class DiagnosisRestController {
    private final DiagnosisService diagnosisService;

    @GetMapping
    public ResponseEntity<List<DiagnosisInfoDto>> getAllDiagnoses() {
        return ResponseEntity.ok(diagnosisService.getAllDiagnoses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiagnosisInfoDto> getDiagnosisInfo(@ExistingDiagnosisId @PathVariable Long id) {
        return ResponseEntity.ok(diagnosisService.getDiagnosis(id));
    }

    @PostMapping
    public ResponseEntity<DiagnosisInfoDto> createDiagnosis(@Valid @RequestBody AddDiagnosisDto diagnosisDto) {
        return new ResponseEntity<>(diagnosisService.createDiagnosis(diagnosisDto), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<DiagnosisInfoDto> updateDiagnosis(@Valid @RequestBody UpdateDiagnosisDto diagnosisDto) {
        return ResponseEntity.ok(diagnosisService.updateDiagnosis(diagnosisDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteDiagnose(@ExistingDiagnosisId @PathVariable Long id) {
        diagnosisService.deleteDiagnosis(id);
        Map<String, Object> response = ResponseService.generateGeneralResponse("Diagnosis with the given ID deleted");
        return ResponseEntity.ok(response);
    }
}

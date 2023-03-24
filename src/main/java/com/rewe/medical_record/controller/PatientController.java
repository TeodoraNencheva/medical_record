package com.rewe.medical_record.controller;

import com.rewe.medical_record.data.dto.patient.AddPatientDto;
import com.rewe.medical_record.data.dto.patient.PatientInfoDTO;
import com.rewe.medical_record.data.dto.patient.UpdatePatientDto;
import com.rewe.medical_record.service.PatientService;
import com.rewe.medical_record.service.ResponseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @GetMapping
    public ResponseEntity<List<PatientInfoDTO>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientInfoDTO> getPatientInfo(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientInfo(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PatientInfoDTO> addPatient(@Valid @RequestBody AddPatientDto patientDto) {
        return new ResponseEntity<>(patientService.addPatient(patientDto), HttpStatus.CREATED);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PatientInfoDTO> updatePatient(@Valid @RequestBody UpdatePatientDto patientDto) {
        return new ResponseEntity<>(patientService.updatePatient(patientDto), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);

        Map<String, Object> body = ResponseService.generateGeneralResponse("Patient with the given ID deleted");
        return new ResponseEntity<>(body, HttpStatus.OK);
    }
}

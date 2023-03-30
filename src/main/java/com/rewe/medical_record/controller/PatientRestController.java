package com.rewe.medical_record.controller;

import com.rewe.medical_record.data.dto.patient.AddPatientDto;
import com.rewe.medical_record.data.dto.patient.PatientInfoDTO;
import com.rewe.medical_record.data.dto.patient.UpdatePatientDto;
import com.rewe.medical_record.service.PatientService;
import com.rewe.medical_record.service.ResponseService;
import com.rewe.medical_record.validation.ExistingPatientId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
@Validated
public class PatientRestController {
    private final PatientService patientService;

    @GetMapping
    public ResponseEntity<List<PatientInfoDTO>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientInfoDTO> getPatientInfo(@ExistingPatientId @PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientInfo(id));
    }

    @PostMapping
    public ResponseEntity<PatientInfoDTO> addPatient(@Valid @RequestBody AddPatientDto patientDto) {
        return new ResponseEntity<>(patientService.addPatient(patientDto), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<PatientInfoDTO> updatePatient(@Valid @RequestBody UpdatePatientDto patientDto) {
        return new ResponseEntity<>(patientService.updatePatient(patientDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePatient(@ExistingPatientId @PathVariable Long id) {
        patientService.deletePatient(id);

        Map<String, Object> body = ResponseService.generateGeneralResponse("Patient with the given ID deleted");
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    /*
    query 1
     */
    @GetMapping("/insured")
    public ResponseEntity<List<PatientInfoDTO>> getAllInsuredPatients() {
        return ResponseEntity.ok(patientService.getAllInsuredPatients());
    }

    /*
    query 2
     */
    @GetMapping("/not-insured-percentage")
    public BigDecimal getNotInsuredPatientsPercentage() {
        return patientService.getNotInsuredPatientsPercentage();
    }
}

package com.rewe.medical_record.controller;

import com.rewe.medical_record.data.dto.doctor.AddDoctorDto;
import com.rewe.medical_record.data.dto.doctor.DoctorInfoDto;
import com.rewe.medical_record.data.dto.doctor.UpdateDoctorDto;
import com.rewe.medical_record.service.DoctorService;
import com.rewe.medical_record.service.ResponseService;
import com.rewe.medical_record.validation.ExistingDoctorId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/doctor")
@RequiredArgsConstructor
@Validated
public class DoctorController {
    private final DoctorService doctorService;
    @GetMapping
    public ResponseEntity<List<DoctorInfoDto>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }
    @GetMapping("/{id}")
    public ResponseEntity<DoctorInfoDto> getDoctorInfo(@ExistingDoctorId @PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorInfo(id));
    }

    @PostMapping
    public ResponseEntity<DoctorInfoDto> addDoctor(@Valid @RequestBody AddDoctorDto doctorDto) {
        return new ResponseEntity<>(doctorService.addDoctor(doctorDto), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<DoctorInfoDto> updateDoctor(@Valid @RequestBody UpdateDoctorDto doctorDto) {
        return new ResponseEntity<>(doctorService.updateDoctor(doctorDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteDoctor(@ExistingDoctorId @PathVariable Long id) {
        doctorService.deleteDoctor(id);

        Map<String, Object> body = ResponseService.generateGeneralResponse("Doctor with the given ID deleted");
        return new ResponseEntity<>(body, HttpStatus.OK);
    }
}

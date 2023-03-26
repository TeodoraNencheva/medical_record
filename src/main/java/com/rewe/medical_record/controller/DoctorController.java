package com.rewe.medical_record.controller;

import com.rewe.medical_record.data.dto.doctor.DoctorInfoDto;
import com.rewe.medical_record.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/doctor")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;
    @GetMapping
    public ResponseEntity<List<DoctorInfoDto>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }
    @GetMapping("/{id}")
    public ResponseEntity<DoctorInfoDto> getDoctorInfo(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorInfo(id));
    }
}

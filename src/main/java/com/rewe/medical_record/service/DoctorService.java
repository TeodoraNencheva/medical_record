package com.rewe.medical_record.service;

import com.rewe.medical_record.data.dto.doctor.DoctorInfoDto;
import com.rewe.medical_record.data.entity.DoctorEntity;
import com.rewe.medical_record.data.repository.DoctorRepository;
import com.rewe.medical_record.exceptions.DoctorNotFoundException;
import com.rewe.medical_record.mapper.DoctorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    public List<DoctorInfoDto> getAllDoctors() {
        return doctorRepository.findAll()
                .stream()
                .map(doctorMapper::doctorEntityToDoctorInfoDto)
                .toList();
    }

    public DoctorInfoDto getDoctorInfo(Long id) {
        DoctorEntity doctorEntity = doctorRepository.findById(id).orElseThrow(() -> new DoctorNotFoundException(id));
        return doctorMapper.doctorEntityToDoctorInfoDto(doctorEntity);
    }
}

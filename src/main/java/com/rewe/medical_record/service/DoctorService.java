package com.rewe.medical_record.service;

import com.rewe.medical_record.data.dto.doctor.AddDoctorDto;
import com.rewe.medical_record.data.dto.doctor.DoctorInfoDto;
import com.rewe.medical_record.data.dto.doctor.UpdateDoctorDto;
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
        return doctorRepository.findAllByDeletedFalse()
                .stream()
                .map(doctorMapper::doctorEntityToDoctorInfoDto)
                .toList();
    }

    public DoctorInfoDto getDoctorInfo(Long id) {
        DoctorEntity doctorEntity = doctorRepository.findAllByIdAndDeletedFalse(id).orElseThrow(() -> new DoctorNotFoundException(id));
        return doctorMapper.doctorEntityToDoctorInfoDto(doctorEntity);
    }

    public DoctorInfoDto addDoctor(AddDoctorDto doctorDto) {
        DoctorEntity saved = doctorRepository.save(doctorMapper.addDoctorDtoToDoctorEntity(doctorDto));
        return doctorMapper.doctorEntityToDoctorInfoDto(saved);
    }

    public DoctorInfoDto updateDoctor(UpdateDoctorDto doctorDto) {
        DoctorEntity toChange = doctorRepository
                .findAllByIdAndDeletedFalse(doctorDto.getId())
                .orElseThrow(() -> new DoctorNotFoundException(doctorDto.getId()));
        DoctorEntity newProperties = doctorMapper.updateDoctorDtoToDoctorEntity(doctorDto);
        toChange.setName(doctorDto.getName());
        toChange.setSpecialties(newProperties.getSpecialties());
        doctorRepository.save(toChange);
        return doctorMapper.doctorEntityToDoctorInfoDto(toChange);
    }

    public void deleteDoctor(Long id) {
        DoctorEntity doctorEntity = doctorRepository.findById(id).orElseThrow(() -> new DoctorNotFoundException(id));
        doctorEntity.setDeleted(true);
        doctorRepository.save(doctorEntity);
    }
}

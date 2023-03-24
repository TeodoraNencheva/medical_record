package com.rewe.medical_record.service;

import com.rewe.medical_record.data.dto.patient.AddPatientDto;
import com.rewe.medical_record.data.dto.patient.PatientInfoDTO;
import com.rewe.medical_record.data.dto.patient.UpdatePatientDto;
import com.rewe.medical_record.data.entity.PatientEntity;
import com.rewe.medical_record.data.repository.PatientRepository;
import com.rewe.medical_record.exceptions.PatientNotFoundException;
import com.rewe.medical_record.mapper.PatientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public List<PatientInfoDTO> getAllPatients() {
        return patientRepository
                .findAll()
                .stream()
                .map(patientMapper::patientEntityToPatientInfoDto)
                .toList();
    }

    public PatientInfoDTO getPatientInfo(Long id) {
        PatientEntity patientEntity = patientRepository
                .findById(id)
                .orElseThrow(() -> new PatientNotFoundException(id));
        return patientMapper.patientEntityToPatientInfoDto(patientEntity);
    }

    public PatientInfoDTO addPatient(AddPatientDto patientDto) {
        PatientEntity patient = patientRepository.save(patientMapper.addPatientDtoToPatientEntity(patientDto));
        return getPatientInfo(patient.getId());
    }

    public PatientInfoDTO updatePatient(UpdatePatientDto patientDto) {
        if (patientRepository.findById(patientDto.getId()).isEmpty()) {
            throw new PatientNotFoundException(patientDto.getId());
        }

        PatientEntity toSave = patientMapper.updatePatientDtoToPatientEntity(patientDto);
        return patientMapper.patientEntityToPatientInfoDto(patientRepository.save(toSave));
    }

    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }
}

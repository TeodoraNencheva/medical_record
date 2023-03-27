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
                .findAllByDeletedFalse()
                .stream()
                .map(patientMapper::patientEntityToPatientInfoDto)
                .toList();
    }

    public PatientInfoDTO getPatientInfo(Long id) {
        PatientEntity patientEntity = patientRepository
                .findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new PatientNotFoundException(id));
        return patientMapper.patientEntityToPatientInfoDto(patientEntity);
    }

    public PatientInfoDTO addPatient(AddPatientDto patientDto) {
        PatientEntity patient = patientRepository.save(patientMapper.addPatientDtoToPatientEntity(patientDto));
        return patientMapper.patientEntityToPatientInfoDto(patient);
    }

    public PatientInfoDTO updatePatient(UpdatePatientDto patientDto) {
        PatientEntity toSave = patientMapper.updatePatientDtoToPatientEntity(patientDto);
        return patientMapper.patientEntityToPatientInfoDto(patientRepository.save(toSave));
    }

    public void deletePatient(Long id) {
        PatientEntity patient = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException(id));
        patient.setDeleted(true);
        patientRepository.save(patient);
    }
}

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

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        if (patientRepository.findByIdAndDeletedFalse(patientDto.getId()).isEmpty()) {
            throw new PatientNotFoundException(patientDto.getId());
        }

        PatientEntity toSave = patientMapper.updatePatientDtoToPatientEntity(patientDto);
        return patientMapper.patientEntityToPatientInfoDto(patientRepository.save(toSave));
    }

    public void deletePatient(Long id) {
        PatientEntity patient = patientRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new PatientNotFoundException(id));
        patient.setDeleted(true);
        patientRepository.save(patient);
    }

    public List<PatientInfoDTO> getAllInsuredPatients() {
        return patientRepository
                .findAllByInsuredTrueAndDeletedFalse()
                .stream()
                .map(patientMapper::patientEntityToPatientInfoDto)
                .toList();
    }

    public BigDecimal getNotInsuredPatientsPercentage() {
        double result = ((1.0 * patientRepository.countAllByInsuredFalseAndDeletedFalse()) / patientRepository.countAllByDeletedFalse()) * 100;
        return new BigDecimal(result).setScale(2, RoundingMode.HALF_UP);
    }
}

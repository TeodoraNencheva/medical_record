package com.rewe.medical_record.service;

import com.rewe.medical_record.data.dto.diagnosis.AddDiagnosisDto;
import com.rewe.medical_record.data.dto.diagnosis.DiagnosisInfoDto;
import com.rewe.medical_record.data.dto.diagnosis.UpdateDiagnosisDto;
import com.rewe.medical_record.data.entity.DiagnosisEntity;
import com.rewe.medical_record.data.repository.DiagnosisRepository;
import com.rewe.medical_record.exceptions.DiagnosisNotFoundException;
import com.rewe.medical_record.mapper.DiagnosisMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiagnosisService {
    private final DiagnosisRepository diagnosisRepository;
    private final DiagnosisMapper diagnosisMapper;

    public List<DiagnosisInfoDto> getAllDiagnoses() {
        return diagnosisRepository
                .findAllByDeletedFalse()
                .stream()
                .map(diagnosisMapper::diagnosisEntityToDiagnosisInfoDto)
                .toList();
    }

    public DiagnosisInfoDto getDiagnosis(Long id) {
        DiagnosisEntity diagnosis = diagnosisRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new DiagnosisNotFoundException(id));
        return diagnosisMapper.diagnosisEntityToDiagnosisInfoDto(diagnosis);
    }

    public DiagnosisInfoDto createDiagnosis(AddDiagnosisDto diagnosisDto) {
        DiagnosisEntity diagnosisEntity = diagnosisMapper.addDiagnosisDtoToDiagnosisEntity(diagnosisDto);
        DiagnosisEntity saved = diagnosisRepository.save(diagnosisEntity);
        return diagnosisMapper.diagnosisEntityToDiagnosisInfoDto(saved);
    }

    public DiagnosisInfoDto updateDiagnosis(UpdateDiagnosisDto diagnosisDto) {
        Long id = diagnosisDto.getId();
        DiagnosisEntity toUpdate = diagnosisRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new DiagnosisNotFoundException(id));
        DiagnosisEntity newProperties = diagnosisMapper.updateDiagnosisDtoToDiagnosisEntity(diagnosisDto);
        toUpdate.setName(newProperties.getName());
        toUpdate.setDescription(newProperties.getDescription());
        DiagnosisEntity saved = diagnosisRepository.save(toUpdate);
        return diagnosisMapper.diagnosisEntityToDiagnosisInfoDto(saved);
    }

    public void deleteDiagnosis(Long id) {
        DiagnosisEntity diagnosis = diagnosisRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new DiagnosisNotFoundException(id));
        diagnosis.setDeleted(true);
        diagnosisRepository.save(diagnosis);
    }
}

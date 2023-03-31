package com.rewe.medical_record.service;

import com.rewe.medical_record.data.dto.visit.AddVisitDto;
import com.rewe.medical_record.data.dto.visit.UpdateVisitDto;
import com.rewe.medical_record.data.dto.visit.VisitInfoDto;
import com.rewe.medical_record.data.entity.DiagnosisEntity;
import com.rewe.medical_record.data.entity.DoctorEntity;
import com.rewe.medical_record.data.entity.PatientEntity;
import com.rewe.medical_record.data.entity.VisitEntity;
import com.rewe.medical_record.data.repository.DiagnosisRepository;
import com.rewe.medical_record.data.repository.DoctorRepository;
import com.rewe.medical_record.data.repository.PatientRepository;
import com.rewe.medical_record.data.repository.VisitRepository;
import com.rewe.medical_record.exceptions.DiagnosisNotFoundException;
import com.rewe.medical_record.exceptions.DoctorNotFoundException;
import com.rewe.medical_record.exceptions.PatientNotFoundException;
import com.rewe.medical_record.exceptions.VisitNotFoundException;
import com.rewe.medical_record.mapper.VisitMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class VisitService {
    private final VisitRepository visitRepository;
    private final VisitMapper visitMapper;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DiagnosisRepository diagnosisRepository;

    public List<VisitInfoDto> getAllVisits() {
        return visitRepository
                .findAllByDeletedFalse()
                .stream()
                .map(visitMapper::visitEntityToVisitInfoDto)
                .toList();
    }

    public VisitInfoDto getVisitInfo(Long id) {
        VisitEntity visitEntity = visitRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new VisitNotFoundException(id));
        return visitMapper.visitEntityToVisitInfoDto(visitEntity);
    }

    public VisitInfoDto addVisit(AddVisitDto addVisitDto) {
        VisitEntity toSave = visitMapper.addVisitDtoToVisitEntity(addVisitDto);
        VisitEntity saved = visitRepository.save(toSave);
        return visitMapper.visitEntityToVisitInfoDto(saved);
    }

    public VisitInfoDto updateVisit(UpdateVisitDto updateVisitDto) {
        VisitEntity toUpdate = visitRepository.findByIdAndDeletedFalse(updateVisitDto.getId())
                .orElseThrow(() -> new VisitNotFoundException(updateVisitDto.getId()));

        VisitEntity newProperties = visitMapper.updateVisitDtoToVisitEntity(updateVisitDto);
        toUpdate.setDoctor(newProperties.getDoctor());
        toUpdate.setPatient(newProperties.getPatient());
        toUpdate.setDiagnoses(newProperties.getDiagnoses());
        toUpdate.setPaidByPatient(newProperties.isPaidByPatient());
        VisitEntity saved = visitRepository.save(toUpdate);
        return visitMapper.visitEntityToVisitInfoDto(saved);
    }

    public void deleteVisit(Long id) {
        VisitEntity visitEntity = visitRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new VisitNotFoundException(id));
        visitEntity.setDeleted(true);
        visitRepository.save(visitEntity);
    }

    public BigDecimal getTotalVisitsIncome() {
        return visitRepository.getAllVisitsIncome().setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getVisitsIncomeByDoctorId(Long doctorId) {
        DoctorEntity doctorEntity = doctorRepository.findById(doctorId).orElseThrow(() -> new DoctorNotFoundException(doctorId));
        return visitRepository.getVisitsIncomeByDoctor(doctorEntity);
    }

    public long countAllByPatientId(Long patientId) {
        PatientEntity patient = patientRepository.findById(patientId).orElseThrow(() -> new PatientNotFoundException(patientId));
        return visitRepository.countAllByPatient(patient);
    }

    public long countAllByContainingDiagnosisId(Long diagnosisId) {
        DiagnosisEntity diagnosisEntity = diagnosisRepository.findById(diagnosisId).orElseThrow(() -> new DiagnosisNotFoundException(diagnosisId));
        return visitRepository.countAllByDiagnosesContaining(diagnosisEntity);
    }

    public BigDecimal getVisitsIncomeByDiagnosisId(Long diagnosisId) {
        DiagnosisEntity diagnosisEntity = diagnosisRepository.findById(diagnosisId).orElseThrow(() -> new DiagnosisNotFoundException(diagnosisId));
        return visitRepository.getVisitsIncomeByDiagnosis(diagnosisEntity);
    }

    public BigDecimal getVisitsIncomeByNonInsuredPatients() {
        return visitRepository.getVisitsIncomeByNonInsuredPatients();
    }
}

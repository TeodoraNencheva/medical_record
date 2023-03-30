package com.rewe.medical_record.service;

import com.rewe.medical_record.data.dto.visit.AddVisitDto;
import com.rewe.medical_record.data.dto.visit.UpdateVisitDto;
import com.rewe.medical_record.data.dto.visit.VisitInfoDto;
import com.rewe.medical_record.data.entity.VisitEntity;
import com.rewe.medical_record.data.repository.VisitRepository;
import com.rewe.medical_record.exceptions.VisitNotFoundException;
import com.rewe.medical_record.mapper.VisitMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitService {
    private final VisitRepository visitRepository;
    private final VisitMapper visitMapper;

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
        return visitRepository.getVisitsIncomeByDoctorId(doctorId);
    }

    public long countAllByPatientId(Long patientId) {
        return visitRepository.countAllByPatientId(patientId);
    }

    public long countAllByContainingDiagnosisId(Long diagnosisId) {
        return visitRepository.countAllByContainingDiagnosisId(diagnosisId);
    }
}

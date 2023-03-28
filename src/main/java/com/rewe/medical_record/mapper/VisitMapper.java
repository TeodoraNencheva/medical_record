package com.rewe.medical_record.mapper;

import com.rewe.medical_record.data.dto.visit.AddVisitDto;
import com.rewe.medical_record.data.dto.visit.UpdateVisitDto;
import com.rewe.medical_record.data.dto.visit.VisitInfoDto;
import com.rewe.medical_record.data.entity.*;
import com.rewe.medical_record.data.repository.DiagnosisRepository;
import com.rewe.medical_record.data.repository.DoctorRepository;
import com.rewe.medical_record.data.repository.FeeHistoryRepository;
import com.rewe.medical_record.data.repository.PatientRepository;
import com.rewe.medical_record.exceptions.DiagnosisNotFoundException;
import com.rewe.medical_record.exceptions.DoctorNotFoundException;
import com.rewe.medical_record.exceptions.PatientNotFoundException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class VisitMapper {
    @Autowired
    protected PatientRepository patientRepository;
    @Autowired
    protected DoctorRepository doctorRepository;
    @Autowired
    protected FeeHistoryRepository feeHistoryRepository;
    @Autowired
    protected DiagnosisRepository diagnosisRepository;

    @Mapping(source = "patient.id", target = "patientId")
    @Mapping(source = "doctor.id", target = "doctorId")
    @Mapping(source = "fee.price", target = "feePrice")
    @Mapping(target = "diagnoses", source = "diagnoses", qualifiedByName = "diagnosesNames")
    public abstract VisitInfoDto visitEntityToVisitInfoDto(VisitEntity visitEntity);

    @Mapping(target = "doctor", source = "doctorId", qualifiedByName = "getDoctorById")
    @Mapping(target = "patient", source = "patientId", qualifiedByName = "getPatientById")
    @Mapping(target = "diagnoses", source = "diagnoses", qualifiedByName = "getDiagnoses")
    @Mapping(target = "fee", expression = "java(feeHistoryRepository.findTopByOrderByEffectiveFromDesc().orElseThrow())")
    @Mapping(target = "paidByPatient", source = "patientId", qualifiedByName = "getPaidByPatient")
    public abstract VisitEntity addVisitDtoToVisitEntity(AddVisitDto addVisitDto);

    @Mapping(target = "doctor", source = "doctorId", qualifiedByName = "getDoctorById")
    @Mapping(target = "patient", source = "patientId", qualifiedByName = "getPatientById")
    @Mapping(target = "diagnoses", source = "diagnoses", qualifiedByName = "getDiagnoses")
    @Mapping(target = "paidByPatient", source = "patientId", qualifiedByName = "getPaidByPatient")
    public abstract VisitEntity updateVisitDtoToVisitEntity(UpdateVisitDto updateVisitDto);

    @Named("diagnosesNames")
    protected Set<String> getDiagnosesNames(Set<DiagnosisEntity> diagnosisEntitySet) {
        return diagnosisEntitySet.stream().map(DiagnosisEntity::getName).collect(Collectors.toSet());
    }

    @Named("getDoctorById")
    protected DoctorEntity getDoctorById(Long id) {
        return doctorRepository
                .findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new DoctorNotFoundException(id));
    }

    @Named("getPatientById")
    protected PatientEntity getPatientById(Long id) {
        return patientRepository
                .findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new PatientNotFoundException(id));
    }

    @Named("getDiagnoses")
    protected Set<DiagnosisEntity> getDiagnoses(Set<Long> diagnosesIds) {
        if (Objects.isNull(diagnosesIds)) {
            return Collections.emptySet();
        }

        return diagnosesIds.stream()
                .map(id -> diagnosisRepository
                        .findById(id)
                        .orElseThrow(() -> new DiagnosisNotFoundException(id)))
                .collect(Collectors.toSet());
    }

    @Named("getPaidByPatient")
    protected boolean getPaidByPatient(Long patientId) {
        return !getPatientById(patientId).isInsured();
    }
}

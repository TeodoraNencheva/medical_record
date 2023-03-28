package com.rewe.medical_record.mapper;

import com.rewe.medical_record.data.repository.DoctorRepository;
import com.rewe.medical_record.data.repository.FeeHistoryRepository;
import com.rewe.medical_record.data.repository.PatientRepository;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class VisitMapper {
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private FeeHistoryRepository feeHistoryRepository;
}

package com.rewe.medical_record.mapper;

import com.rewe.medical_record.data.dto.patient.AddPatientDto;
import com.rewe.medical_record.data.dto.patient.PatientInfoDTO;
import com.rewe.medical_record.data.dto.patient.UpdatePatientDto;
import com.rewe.medical_record.data.entity.PatientEntity;
import com.rewe.medical_record.data.repository.GeneralPractitionerRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class PatientMapper {
    @Autowired
    public GeneralPractitionerRepository gpRepository;

    @Mapping(source = "gp.id", target = "gpId")
    public abstract PatientInfoDTO patientEntityToPatientInfoDto(PatientEntity patientEntity);
    @Mapping(expression = "java(gpRepository.findById(patientDto.getGpId()).get())", target = "gp")
    public abstract PatientEntity addPatientDtoToPatientEntity(AddPatientDto patientDto);
    @Mapping(expression = "java(gpRepository.findById(patientDto.getGpId()).get())", target = "gp")
    public abstract PatientEntity updatePatientDtoToPatientEntity(UpdatePatientDto patientDto);
}

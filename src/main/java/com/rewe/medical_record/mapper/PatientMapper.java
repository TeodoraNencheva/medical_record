package com.rewe.medical_record.mapper;

import com.rewe.medical_record.data.dto.patient.AddPatientDto;
import com.rewe.medical_record.data.dto.patient.PatientInfoDTO;
import com.rewe.medical_record.data.dto.patient.UpdatePatientDto;
import com.rewe.medical_record.data.entity.GeneralPractitionerEntity;
import com.rewe.medical_record.data.entity.PatientEntity;
import com.rewe.medical_record.data.repository.GeneralPractitionerRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

@Mapper(componentModel = "spring")
public abstract class PatientMapper {
    @Autowired
    protected GeneralPractitionerRepository gpRepository;

    @Mapping(source = "gp.id", target = "gpId")
    public abstract PatientInfoDTO patientEntityToPatientInfoDto(PatientEntity patientEntity);
    @Mapping(target = "gp", source = "gpId", qualifiedByName = "setGp")
    public abstract PatientEntity addPatientDtoToPatientEntity(AddPatientDto patientDto);
    @Mapping(target = "gp", source = "gpId", qualifiedByName = "setGp")
    public abstract PatientEntity updatePatientDtoToPatientEntity(UpdatePatientDto patientDto);

    @Named("setGp")
    protected GeneralPractitionerEntity setGp(Long gpId) {
        if (Objects.isNull(gpId)) {
            return null;
        }

        return gpRepository.findById(gpId).orElse(null);
    }
}

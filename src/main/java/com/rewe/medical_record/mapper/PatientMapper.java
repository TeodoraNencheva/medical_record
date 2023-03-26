package com.rewe.medical_record.mapper;

import com.rewe.medical_record.data.dto.patient.AddPatientDto;
import com.rewe.medical_record.data.dto.patient.PatientInfoDTO;
import com.rewe.medical_record.data.dto.patient.UpdatePatientDto;
import com.rewe.medical_record.data.entity.GeneralPractitionerEntity;
import com.rewe.medical_record.data.entity.PatientEntity;
import com.rewe.medical_record.data.repository.GeneralPractitionerRepository;
import com.rewe.medical_record.exceptions.GeneralPractitionerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PatientMapper {
    private final GeneralPractitionerRepository gpRepository;
    private final ModelMapper mapper = new ModelMapper();

    public PatientInfoDTO patientEntityToPatientInfoDto(PatientEntity patientEntity) {
        PatientInfoDTO result = this.mapper.map(patientEntity, PatientInfoDTO.class);
        result.setGpId(patientEntity.getGp().getId());
        return result;
    }

    public PatientEntity addPatientDtoToPatientEntity(AddPatientDto patientDto) {
        PatientEntity result = this.mapper.map(patientDto, PatientEntity.class);
        GeneralPractitionerEntity gp = gpRepository
                .findById(patientDto.getGpId())
                .orElseThrow(() -> new GeneralPractitionerNotFoundException(patientDto.getGpId()));
        result.setGp(gp);
        result.setId(null);
        return result;
    }

    public PatientEntity updatePatientDtoToPatientEntity(UpdatePatientDto patientDto) {
        PatientEntity result = this.mapper.map(patientDto, PatientEntity.class);
        GeneralPractitionerEntity gp = gpRepository
                .findById(patientDto.getGpId())
                .orElseThrow(() -> new GeneralPractitionerNotFoundException(patientDto.getGpId()));
        result.setGp(gp);
        return result;
    }
}

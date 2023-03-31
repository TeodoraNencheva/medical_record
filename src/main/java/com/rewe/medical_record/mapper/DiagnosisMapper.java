package com.rewe.medical_record.mapper;

import com.rewe.medical_record.data.dto.diagnosis.AddDiagnosisDto;
import com.rewe.medical_record.data.dto.diagnosis.DiagnosisInfoDto;
import com.rewe.medical_record.data.dto.diagnosis.UpdateDiagnosisDto;
import com.rewe.medical_record.data.entity.DiagnosisEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DiagnosisMapper {
    DiagnosisInfoDto diagnosisEntityToDiagnosisInfoDto(DiagnosisEntity diagnosis);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    DiagnosisEntity addDiagnosisDtoToDiagnosisEntity(AddDiagnosisDto addDiagnosisDto);

    @Mapping(target = "deleted", ignore = true)
    DiagnosisEntity updateDiagnosisDtoToDiagnosisEntity(UpdateDiagnosisDto updateDiagnosisDto);
}

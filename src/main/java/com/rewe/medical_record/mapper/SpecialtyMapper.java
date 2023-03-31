package com.rewe.medical_record.mapper;

import com.rewe.medical_record.data.dto.specialty.AddSpecialtyDto;
import com.rewe.medical_record.data.dto.specialty.SpecialtyInfoDto;
import com.rewe.medical_record.data.dto.specialty.UpdateSpecialtyDto;
import com.rewe.medical_record.data.entity.SpecialtyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SpecialtyMapper {
    SpecialtyInfoDto specialtyEntityToSpecialtyInfoDto(SpecialtyEntity specialty);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    SpecialtyEntity addSpecialtyDtoToSpecialtyEntity(AddSpecialtyDto addSpecialtyDto);

    @Mapping(target = "deleted", ignore = true)
    SpecialtyEntity updateSpecialtyDtoToSpecialtyEntity(UpdateSpecialtyDto specialtyDto);
}

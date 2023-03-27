package com.rewe.medical_record.mapper;

import com.rewe.medical_record.data.dto.doctor.AddDoctorDto;
import com.rewe.medical_record.data.dto.doctor.DoctorInfoDto;
import com.rewe.medical_record.data.dto.doctor.UpdateDoctorDto;
import com.rewe.medical_record.data.entity.DoctorEntity;
import com.rewe.medical_record.data.entity.SpecialtyEntity;
import com.rewe.medical_record.data.repository.SpecialtyRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class DoctorMapper {
    @Autowired
    protected SpecialtyRepository specialtyRepository;

    @Mapping(target = "specialties", source = "specialties", qualifiedByName = "specialtiesNames")
    public abstract DoctorInfoDto doctorEntityToDoctorInfoDto(DoctorEntity doctorEntity);

    @Mapping(target = "specialties", source = "specialties", qualifiedByName = "specialtyEntities")
    public abstract DoctorEntity addDoctorDtoToDoctorEntity(AddDoctorDto addDoctorDto);

    @Mapping(target = "specialties", source = "specialties", qualifiedByName = "specialtyEntities")
    public abstract DoctorEntity updateDoctorDtoToDoctorEntity(UpdateDoctorDto updateDoctorDto);

    @Named("specialtiesNames")
    protected static Set<String> getSpecialtiesNames(Set<SpecialtyEntity> specialtyEntities) {
        return specialtyEntities.stream().map(SpecialtyEntity::getName).collect(Collectors.toSet());
    }

    @Named("specialtyEntities")
    protected Set<SpecialtyEntity> getSpecialtyEntities(Set<String> specialtyNames) {
        return specialtyNames.stream().map(n -> specialtyRepository.findByName(n).orElse(null)).collect(Collectors.toSet());
    }
}

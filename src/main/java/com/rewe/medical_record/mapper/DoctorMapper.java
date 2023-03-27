package com.rewe.medical_record.mapper;

import com.rewe.medical_record.data.dto.doctor.DoctorInfoDto;
import com.rewe.medical_record.data.entity.DoctorEntity;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class DoctorMapper {
    private final ModelMapper mapper = new ModelMapper();

    public DoctorInfoDto doctorEntityToDoctorInfoDto(DoctorEntity doctorEntity) {
        return this.mapper.map(doctorEntity, DoctorInfoDto.class);
    }
}
